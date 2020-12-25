package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.config.exception.GlobalException;
import org.jc.backend.dao.InboundEntryMapper;
import org.jc.backend.dao.ModificationMapper;
import org.jc.backend.entity.DO.InboundEntryDO;
import org.jc.backend.entity.DO.InboundEntryModifyDO;
import org.jc.backend.entity.DO.ModificationDO;
import org.jc.backend.entity.InboundProductModifyO;
import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.InboundEntryCompleteO;
import org.jc.backend.entity.VO.InboundEntryModifyVO;
import org.jc.backend.entity.VO.InboundEntryWithProductsVO;
import org.jc.backend.service.InboundEntryService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class InboundEntryServiceImpl implements InboundEntryService {
    private static final Logger logger = LoggerFactory.getLogger(InboundEntryServiceImpl.class);

    private final InboundEntryMapper inboundEntryMapper;
    private final ModificationMapper modificationMapper;

    public InboundEntryServiceImpl(InboundEntryMapper inboundEntryMapper, ModificationMapper modificationMapper) {
        this.inboundEntryMapper = inboundEntryMapper;
        this.modificationMapper = modificationMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    public void createEntry(InboundEntryWithProductsVO entryWithProductsVO) throws GlobalException {
        InboundEntryDO newEntry = new InboundEntryDO();
        BeanUtils.copyProperties(entryWithProductsVO, newEntry);
        List<InboundProductO> newProducts = entryWithProductsVO.getInboundProducts();

        int count = inboundEntryMapper.countNumberOfEntriesOfToday();
        String newSerial = MyUtils.formNewSerial("入库", count);

        newEntry.setInboundEntryID(newSerial);
        try {
            inboundEntryMapper.insertNewEntry(newEntry);
        } catch (PersistenceException e) {
            e.printStackTrace();
            logger.error("Insert new inbound entry failed");
            throw new GlobalException("Insert new inbound entry failed");
        }

        for (var product : newProducts) {
            product.setInboundEntryID(newSerial);
            try {
                int id = inboundEntryMapper.insertNewProduct(product);
                logger.info("Insert new inbound product id: " + id);
            } catch (PersistenceException e) {
                e.printStackTrace();
                logger.error("Insert new inbound product failed");
                throw new GlobalException("Insert new inbound product failed");
                //todo fallback previous inserts
            }
        }
    }

    public List<InboundEntryWithProductsVO> getEntriesInDateRangeByCompanyID(Date startDate, Date endDate, int id) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<InboundEntryDO> entriesFromDatabase = inboundEntryMapper.queryEntriesInDateRangeByCompanyID(
                dateFormat.format(startDate), dateFormat.format(endDate), id);

        List<InboundEntryWithProductsVO> entries = new ArrayList<>();
        for (var entryFromDatabase : entriesFromDatabase) {
            InboundEntryWithProductsVO tempEntry = new InboundEntryWithProductsVO();
            BeanUtils.copyProperties(entryFromDatabase, tempEntry);

            try {
                List<InboundProductO> products = inboundEntryMapper.queryProductsByEntryID(
                        tempEntry.getInboundEntryID());
                tempEntry.setInboundProducts(products);
            } catch (PersistenceException e) {
                logger.error("");
                //todo
            }

            entries.add(tempEntry);
        }

        return entries;
    }

    public void completeEntry(InboundEntryCompleteO completionO) {
        InboundEntryCompleteO currentInfo = completionO;
        //query database for compare
        String id = completionO.getInboundEntryID();
        InboundEntryCompleteO originInfo;
        try {
            originInfo = (inboundEntryMapper.selectEntryShippingInfoForCompare(id)).get(0);
        } catch (PersistenceException e) {
            e.printStackTrace();
            //todo
            return;
        }

        StringBuilder record = new StringBuilder("修改者: " + currentInfo.getDrawer() + "; ");
        // check changes to shipping info
        boolean bool = MyUtils.shippingInfoCompareAndFormModificationRecord(record, currentInfo, originInfo);

        if (bool) {
            try {
                inboundEntryMapper.updateShippingInfo(currentInfo);

                logger.info("Completion: " + record);
                modificationMapper.insertModificationRecord(new ModificationDO(
                        0, originInfo.getInboundEntryID(), record.toString(),
                        new SimpleDateFormat("yyyy-MM-dd").format(new Date())
                ));
            } catch (PersistenceException e) {
                e.printStackTrace();
                //todo
            }
        }
        else {
            logger.info("nothing changed");
        }
    }

    public void modifyEntry(InboundEntryModifyVO modificationVO) {
        //extract entryDO
        InboundEntryModifyDO currentEntry = new InboundEntryModifyDO();
        BeanUtils.copyProperties(modificationVO, currentEntry);

        //extract List<productO>
        List<InboundProductModifyO> currentProducts = modificationVO.getInboundProducts();

        //query database for compare
        String id = currentEntry.getInboundEntryID();
        logger.info("Serial to be changed: " + id);
        InboundEntryModifyDO originEntry;
        List<InboundProductModifyO> originProducts;
        try {
            originEntry = (inboundEntryMapper.selectEntryForCompare(id)).get(0);
            originProducts = inboundEntryMapper.selectProductsForCompare(id);
        } catch (PersistenceException e) {
            e.printStackTrace();
            //todo
            return;
        }

        //compare entry
        StringBuilder record = new StringBuilder("修改者: " + currentEntry.getDrawer() + "; ");
        boolean bool1 = MyUtils.entryCompareAndFormModificationRecord(record, currentEntry, originEntry);

        if (bool1) {
            try {
                logger.info(""); //todo
                inboundEntryMapper.updateEntry(currentEntry);
            } catch (PersistenceException e) {
                e.printStackTrace();
                //todo
            }
        }

        boolean bool2 = false;
        for (var originProduct : originProducts) {
            boolean found = false;
            for (var currentProduct : currentProducts) {
                if (currentProduct.getInboundProductID() == originProduct.getInboundProductID()) {
                    boolean bool3 = MyUtils.productsCompareAndFormModificationRecord(
                            record, currentProduct, originProduct);

                    if (bool3) {
                        bool2 = true;
                        try {
                            logger.info("");//todo
                            inboundEntryMapper.updateProduct(currentProduct);
                        } catch (PersistenceException e) {
                            e.printStackTrace();
                            //todo
                        }
                    }
                    found = true;
                    break;
                }
            }
            if (!found) {
                try {
                    inboundEntryMapper.deleteProductByID(originProduct.getInboundProductID());
                } catch (PersistenceException e) {
                    e.printStackTrace();
                    //todo
                }
            }
        }

        if (bool1 || bool2) {
            logger.info("Modification: " + record);
            try {
                modificationMapper.insertModificationRecord(new ModificationDO(
                        0, originEntry.getInboundEntryID(), record.toString(),
                        new SimpleDateFormat("yyyy-MM-dd").format(new Date())
                ));
            } catch (PersistenceException e) {
                e.printStackTrace();
                //todo
            }
        }

    }

    public void deleteEntry(String id) {
        try {
            inboundEntryMapper.deleteProductsByEntryID(id);
            inboundEntryMapper.deleteEntry(id);
        } catch (PersistenceException e) {
            logger.error("");
            //todo
        }
    }

}
