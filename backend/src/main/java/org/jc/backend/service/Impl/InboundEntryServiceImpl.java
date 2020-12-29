package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.InboundEntryMapper;
import org.jc.backend.dao.ModificationMapper;
import org.jc.backend.dao.WarehouseStockMapper;
import org.jc.backend.entity.*;
import org.jc.backend.entity.DO.InboundEntryDO;
import org.jc.backend.entity.DO.InboundEntryModifyDO;
import org.jc.backend.entity.VO.InboundEntryModifyVO;
import org.jc.backend.entity.VO.InboundEntryWithProductsVO;
import org.jc.backend.service.InboundEntryService;
import org.jc.backend.utils.IOModificationUtils;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class InboundEntryServiceImpl implements InboundEntryService {
    private static final Logger logger = LoggerFactory.getLogger(InboundEntryServiceImpl.class);

    private final InboundEntryMapper inboundEntryMapper;
    private final ModificationMapper modificationMapper;
    private final WarehouseStockMapper warehouseStockMapper;

    public InboundEntryServiceImpl(InboundEntryMapper inboundEntryMapper,
                                   ModificationMapper modificationMapper,
                                   WarehouseStockMapper warehouseStockMapper) {
        this.inboundEntryMapper = inboundEntryMapper;
        this.modificationMapper = modificationMapper;
        this.warehouseStockMapper = warehouseStockMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    public void createEntry(InboundEntryWithProductsVO entryWithProductsVO) {

        InboundEntryDO newEntry = new InboundEntryDO();
        BeanUtils.copyProperties(entryWithProductsVO, newEntry);
        List<InboundProductO> newProducts = entryWithProductsVO.getInboundProducts();

        try {
            int count = inboundEntryMapper.countNumberOfEntriesOfToday();
            String newSerial = MyUtils.formNewSerial("购入", count);

            newEntry.setInboundEntryID(newSerial);
            //initialize newEntry fields
            newEntry.setShippingCostSerial("");
            newEntry.setReturnDate("");
            newEntry.setReturnSerial("");
            newEntry.setPrintTimes(0);
            newEntry.setIsModified(0);
            inboundEntryMapper.insertNewEntry(newEntry);

            for (var product : newProducts) {
                //set entry serial id for product
                product.setInboundEntryID(newSerial);
                //initialize entry newProduct fields
                product.setReturnStatus(0);
                product.setCheckoutSerial("");
                product.setInvoiceSerial("");

                //check warehouseStock for existence, if not, create new one
                if (warehouseStockMapper.queryWarehouseStocksBySku(product.getSkuID()).size() == 0) {
                    WarehouseStockO newWarehouseStock = new WarehouseStockO(
                            0, product.getSkuID(), product.getWarehouseID(), "",
                            0, 0.0, 0.0, "",
                            0.0, 0.0, 0.0, "",
                            0.0, 0.0, 0.0, 0.0, -1,
                            -1, -1);
                    warehouseStockMapper.insertNewWarehouseStock(newWarehouseStock);
                }

                int id = inboundEntryMapper.insertNewProduct(product);
                logger.info("Insert new inbound product id: " + id);
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production mode
            logger.error("Insert failed");
            throw e;
        }

    }

    @Transactional(readOnly = true)
    public List<InboundEntryWithProductsVO> getEntriesInDateRangeByTypeAndCompanyID(Date startDate, Date endDate,
                                                                                    String type, int companyID) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<InboundEntryWithProductsVO> entries = new ArrayList<>();
        try {
            List<InboundEntryDO> entriesFromDatabase = inboundEntryMapper.queryEntriesInDateRangeByTypeAndCompanyID(
                    dateFormat.format(startDate), dateFormat.format(endDate), type, companyID);

            for (var entryFromDatabase : entriesFromDatabase) {
                InboundEntryWithProductsVO tempEntry = new InboundEntryWithProductsVO();
                BeanUtils.copyProperties(entryFromDatabase, tempEntry);

                List<InboundProductO> products = inboundEntryMapper.queryProductsByEntryID(
                        tempEntry.getInboundEntryID());
                tempEntry.setInboundProducts(products);

                entries.add(tempEntry);
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production mode
            logger.error("Query failed");
            throw e;
        }

        return entries;
    }

    @Transactional
    public void completeEntry(InboundEntryCompleteO currentInfo) {

        String id = currentInfo.getInboundEntryID();

        InboundEntryCompleteO originInfo;
        try {
            //query database for compare
            originInfo = (inboundEntryMapper.selectEntryShippingInfoForCompare(id)).get(0);

            // check changes to shipping info
            StringBuilder record = new StringBuilder("修改者: " + currentInfo.getDrawer() + "; ");
            boolean bool = IOModificationUtils.shippingInfoCompareAndFormModificationRecord(record, currentInfo, originInfo);

            if (bool) {
                inboundEntryMapper.updateShippingInfo(currentInfo);

                logger.info("Completion: " + record);
                modificationMapper.insertModificationRecord(new ModificationO(
                        originInfo.getInboundEntryID(), record.toString()));
            } else {
                logger.warn("Nothing changed!");
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production mode
            logger.error("Completion failed");
            throw e;
        }

    }

    @Transactional
    public void modifyEntry(InboundEntryModifyVO modificationVO) {
        //extract entryDO
        InboundEntryModifyDO currentEntry = new InboundEntryModifyDO();
        BeanUtils.copyProperties(modificationVO, currentEntry);
        currentEntry.setIsModified(1);

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

            //compare entry
            StringBuilder record = new StringBuilder("修改者: " + currentEntry.getDrawer() + "; ");
            boolean bool1 = IOModificationUtils.entryCompareAndFormModificationRecord(record, currentEntry, originEntry);

            if (bool1) {
                inboundEntryMapper.updateEntry(currentEntry);
            }

            boolean bool2 = false;
            for (var originProduct : originProducts) {
                boolean found = false;
                for (var currentProduct : currentProducts) {
                    if (currentProduct.getInboundProductID() == originProduct.getInboundProductID()) {
                        boolean bool3 = IOModificationUtils.productsCompareAndFormModificationRecord(
                                record, currentProduct, originProduct);

                        if (bool3) {
                            bool2 = true;
                            inboundEntryMapper.updateProduct(currentProduct);
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    inboundEntryMapper.deleteProductByID(originProduct.getInboundProductID());
                }
            }

            if (bool1 || bool2) {
                logger.info("Modification: " + record);
                modificationMapper.insertModificationRecord(new ModificationO(
                        originEntry.getInboundEntryID(), record.toString()));
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production mode
            logger.error("Modify failed");
            throw e;
        }

    }

    @Transactional
    public void deleteEntry(String id) {
        try {
            inboundEntryMapper.deleteProductsByEntryID(id);
            inboundEntryMapper.deleteEntry(id);
        } catch (PersistenceException e) {
            logger.error("Deletion failed"); // todo remove in production mode
            throw e;
        }
    }

}
