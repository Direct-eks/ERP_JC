package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.config.exception.GlobalException;
import org.jc.backend.dao.ModificationMapper;
import org.jc.backend.dao.OutboundEntryMapper;
import org.jc.backend.entity.DO.ModificationDO;
import org.jc.backend.entity.DO.OutboundEntryDO;
import org.jc.backend.entity.DO.OutboundEntryModifyDO;
import org.jc.backend.entity.OutboundEntryCompleteO;
import org.jc.backend.entity.OutboundProductModifyO;
import org.jc.backend.entity.OutboundProductO;
import org.jc.backend.entity.VO.OutboundEntryModifyVO;
import org.jc.backend.entity.VO.OutboundEntryWithProductsVO;
import org.jc.backend.service.OutboundEntryService;
import org.jc.backend.utils.IOModificationUtils;
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
public class OutboundEntryServiceImpl implements OutboundEntryService {
    private static final Logger logger = LoggerFactory.getLogger(OutboundEntryServiceImpl.class);

    private final OutboundEntryMapper outboundEntryMapper;
    private final ModificationMapper modificationMapper;

    public OutboundEntryServiceImpl(OutboundEntryMapper outboundEntryMapper, ModificationMapper modificationMapper) {
        this.outboundEntryMapper = outboundEntryMapper;
        this.modificationMapper = modificationMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    public void createEntry(OutboundEntryWithProductsVO entryWithProductsVO) throws GlobalException {
        OutboundEntryDO newEntry = new OutboundEntryDO();
        BeanUtils.copyProperties(entryWithProductsVO, newEntry);
        List<OutboundProductO> newProducts = entryWithProductsVO.getOutboundProducts();

        int count = outboundEntryMapper.countNumberOfEntriesOfToday();
        String newSerial = MyUtils.formNewSerial("销出", count);

        newEntry.setOutboundEntryID(newSerial);
        try {
            outboundEntryMapper.insertNewEntry(newEntry);
        } catch (PersistenceException e) {
            e.printStackTrace();
            logger.error("");
            throw new GlobalException("");//todo
        }

        for (var product : newProducts) {
            product.setOutboundEntryID(newSerial);
            try {
                int id = outboundEntryMapper.insertNewProduct(product);
                logger.info("Insert new outbound product id: " + id);
            } catch (PersistenceException e) {
                e.printStackTrace();
                logger.error("");
                //todo
            }
        }

        //todo: deduct stock
    }

    public List<OutboundEntryWithProductsVO> getEntriesInDateRangeByCompanyID(Date startDate, Date endDate, int id) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<OutboundEntryDO> entriesFromDatabase = outboundEntryMapper.queryEntriesInDateRangeByCompanyID(
                dateFormat.format(startDate), dateFormat.format(endDate), id);

        List<OutboundEntryWithProductsVO> entries = new ArrayList<>();
        for (var entryFromDatabase : entriesFromDatabase) {
            OutboundEntryWithProductsVO tempEntry = new OutboundEntryWithProductsVO();
            BeanUtils.copyProperties(entryFromDatabase, tempEntry);

            try {
                List<OutboundProductO> products = outboundEntryMapper.queryProductsByEntryID(
                        tempEntry.getOutboundEntryID());
                tempEntry.setOutboundProducts(products);
            } catch (PersistenceException e) {
                logger.error("");
                //todo
            }

            entries.add(tempEntry);
        }

        return entries;
    }

    public void completeEntry(OutboundEntryCompleteO completeO) {
        OutboundEntryCompleteO currentInfo = completeO;

        String id = completeO.getOutboundEntryID();
        OutboundEntryCompleteO originInfo;
        try {
            originInfo = (outboundEntryMapper.selectEntryShippingInfoForCompare(id)).get(0);
        } catch (PersistenceException e) {
            e.printStackTrace();
            //todo
            return;
        }

        StringBuilder record = new StringBuilder("修改者: " + currentInfo.getDrawer() + "; ");
        boolean bool = IOModificationUtils.shippingInfoCompareAndFormModificationRecord(record, currentInfo, originInfo);

        if (bool) {
            try {
                outboundEntryMapper.updateShippingInfo(currentInfo);

                logger.info("Completion: " + record);
                modificationMapper.insertModificationRecord(new ModificationDO(
                        0, originInfo.getOutboundEntryID(), record.toString(),
                        new SimpleDateFormat("yyyy-MM-dd").format(new Date())
                ));
            } catch (PersistenceException e) {
                e.printStackTrace();
                //todo
            }
        }
        else {
            logger.info("Nothing changed");
        }
    }

    public void modifyEntry(OutboundEntryModifyVO modifyVO) {
        OutboundEntryModifyDO currentEntry = new OutboundEntryModifyDO();
        BeanUtils.copyProperties(modifyVO, currentEntry);

        List<OutboundProductModifyO> currentProducts = modifyVO.getOutboundProducts();

        String id = currentEntry.getOutboundEntryID();
        logger.info("Serial to be changed: " + id);
        OutboundEntryModifyDO originEntry;
        List<OutboundProductModifyO> originProducts;
        try {
            originEntry = (outboundEntryMapper.selectEntryForCompare(id)).get(0);
            originProducts = outboundEntryMapper.selectProductsForCompare(id);
        } catch (PersistenceException e) {
            e.printStackTrace();
            //todo
            return;
        }

        StringBuilder record = new StringBuilder("修改者: " + currentEntry.getDrawer() + "; ");
        boolean bool1 = IOModificationUtils.entryCompareAndFormModificationRecord(record, currentEntry, originEntry);

        if (bool1) {
            try {
                logger.info("");//todo
                outboundEntryMapper.updateEntry(currentEntry);
            } catch (PersistenceException e) {
                e.printStackTrace();
                //todo
            }
        }

        boolean bool2 = false;
        for (var originProduct : originProducts) {
            boolean found = false;
            for (var currentProduct : currentProducts) {
                if (currentProduct.getOutboundProductID() == originProduct.getOutboundProductID()) {
                    boolean bool3 = IOModificationUtils.productsCompareAndFormModificationRecord(
                            record, currentProduct, originProduct);

                    if (bool3) {
                        bool2 = true;
                        try {
                            logger.info("");//todo
                            outboundEntryMapper.updateProduct(currentProduct);
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
                    outboundEntryMapper.deleteProductByID(originProduct.getOutboundProductID());
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
                        0, originEntry.getOutboundEntryID(), record.toString(),
                        new SimpleDateFormat("yyyy-MM-dd").format(new Date())
                ));
            } catch (PersistenceException e) {
                e.printStackTrace();
                //todo
            }
        }

        //todo: deduct stock
    }

    public void deleteEntry(String id) {
        try {
            outboundEntryMapper.deleteProductsByEntryID(id);
            outboundEntryMapper.deleteEntry(id);
        } catch (PersistenceException e) {
            logger.error("");
            //todo
        }

        //todo: deduct stock
    }
}
