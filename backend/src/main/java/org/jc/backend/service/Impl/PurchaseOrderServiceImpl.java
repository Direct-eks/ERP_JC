package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.ModificationMapper;
import org.jc.backend.dao.PurchaseOrderMapper;
import org.jc.backend.entity.ModificationO;
import org.jc.backend.entity.DO.PurchaseOrderEntryDO;
import org.jc.backend.entity.VO.PurchaseOrderEntryWithProductsVO;
import org.jc.backend.entity.PurchaseOrderProductO;
import org.jc.backend.service.PurchaseOrderService;
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
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);

    private final PurchaseOrderMapper purchaseOrderMapper;
    private final ModificationMapper modificationMapper;

    public PurchaseOrderServiceImpl(PurchaseOrderMapper purchaseOrderMapper, ModificationMapper modificationMapper) {
        this.purchaseOrderMapper = purchaseOrderMapper;
        this.modificationMapper = modificationMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    public void createOrder(PurchaseOrderEntryWithProductsVO entryWithProducts) {

        PurchaseOrderEntryDO newEntry = new PurchaseOrderEntryDO();
        BeanUtils.copyProperties(entryWithProducts, newEntry);
        List<PurchaseOrderProductO> newProducts = entryWithProducts.getPurchaseOrderProducts();

        try {
            // calculate the number of entries have been created for today's date, and generate new serial
            int count = purchaseOrderMapper.countNumberOfEntriesOfToday();
            String newSerial = MyUtils.formNewSerial("采订", count, newEntry.getEntryDate());

            newEntry.setPurchaseOrderEntryID(newSerial);
            purchaseOrderMapper.insertNewOrderEntry(newEntry);

            for (var product : newProducts) {
                product.setPurchaseOrderEntryID(newSerial);
                int id = purchaseOrderMapper.insertNewOrderProduct(product);
                logger.info("Insert new purchase product id: " + id);
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production mode
            logger.error("Insert failed");
            throw e;
        }

    }

    @Transactional(readOnly = true)
    public List<PurchaseOrderEntryWithProductsVO> getOrdersInDateRangeByCompanyID(Date startDate, Date endDate, int companyID) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<PurchaseOrderEntryWithProductsVO> entries = new ArrayList<>();
        try {
            List<PurchaseOrderEntryDO> entriesFromDatabase = purchaseOrderMapper.queryEntriesInDateRangeByCompanyID(
                    dateFormat.format(startDate), dateFormat.format(endDate), companyID);

            for (var entryFromDatabase : entriesFromDatabase) {
                PurchaseOrderEntryWithProductsVO tempEntry = new PurchaseOrderEntryWithProductsVO();
                BeanUtils.copyProperties(entryFromDatabase, tempEntry);

                List<PurchaseOrderProductO> products = purchaseOrderMapper.queryProductsByEntryID(
                        tempEntry.getPurchaseOrderEntryID());
                tempEntry.setPurchaseOrderProducts(products);

                entries.add(tempEntry);
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production mode
            logger.error("Query error");
            throw e;
        }

        return entries;
    }

    @Transactional(readOnly = true)
    public List<PurchaseOrderEntryWithProductsVO> getOrdersByCompanyID(int id) {

        List<PurchaseOrderEntryWithProductsVO> entries = new ArrayList<>();
        try {
            List<PurchaseOrderEntryDO> entriesFromDatabase = purchaseOrderMapper.queryEntriesByCompanyID(id);

            for (var entryFromDatabase : entriesFromDatabase) {
                PurchaseOrderEntryWithProductsVO tempEntry = new PurchaseOrderEntryWithProductsVO();
                BeanUtils.copyProperties(entryFromDatabase, tempEntry);

                List<PurchaseOrderProductO> products = purchaseOrderMapper.queryProductsByEntryID(
                        tempEntry.getPurchaseOrderEntryID());
                tempEntry.setPurchaseOrderProducts(products);

                entries.add(tempEntry);
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production mode
            logger.error("Query error");
            throw e;
        }

        return entries;
    }

    @Transactional
    public void modifyOrder(PurchaseOrderEntryWithProductsVO purchaseOrderEntryWithProductsVO) {
        //extract entryDO
        PurchaseOrderEntryDO currentEntry = new PurchaseOrderEntryDO();
        BeanUtils.copyProperties(purchaseOrderEntryWithProductsVO, currentEntry);

        //extract List<productO>
        List<PurchaseOrderProductO> currentProducts = purchaseOrderEntryWithProductsVO.getPurchaseOrderProducts();

        //query database for compare
        String id = currentEntry.getPurchaseOrderEntryID();
        logger.info("Serial to be changed: " + id);
        PurchaseOrderEntryDO originEntry;
        List<PurchaseOrderProductO> originProducts;
        try {
            originEntry = purchaseOrderMapper.selectEntryForCompare(id);
            originProducts = purchaseOrderMapper.selectProductsForCompare(id);

            //compare entry
            StringBuilder record = new StringBuilder("修改者: " + currentEntry.getDrawer() + "; "); //modification_record
            boolean bool1 = IOModificationUtils.entryCompareAndFormModificationRecord(record, currentEntry, originEntry);

            if (bool1) {
                purchaseOrderMapper.updateOrderEntry(currentEntry);
            }

            boolean bool2 = false; //bool to indicate changes to products
            for (var originProduct : originProducts) {
                boolean found = false;
                for (var currentProduct : currentProducts) {
                    if (currentProduct.getPurchaseOrderProductID() == originProduct.getPurchaseOrderProductID()) {
                        boolean bool3 = IOModificationUtils.productsCompareAndFormModificationRecord(
                                record, currentProduct, originProduct);

                        if (bool3) {
                            bool2 = true; //bool2 here in case only one product is changed and bool2 will be overwritten
                            purchaseOrderMapper.updateOrderProduct(currentProduct);
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) { //entry is removed
                    purchaseOrderMapper.deleteOrderProductByID(originProduct.getPurchaseOrderProductID());
                }
            }

            if (bool1 || bool2) {
                logger.info("Modification: " + record);
                modificationMapper.insertModificationRecord(new ModificationO(
                        originEntry.getPurchaseOrderEntryID(), record.toString()));
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production mode
            logger.error("Update error");
            throw e;
        }

    }

    @Transactional
    public void deleteOrder(String id) {
        try {
            purchaseOrderMapper.deleteOrderProductsByEntryID(id);
            purchaseOrderMapper.deleteOrderEntry(id);
        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production mode
            logger.error("Delete error");
            throw e;
        }
    }

}
