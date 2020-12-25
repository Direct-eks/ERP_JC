package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.config.exception.GlobalException;
import org.jc.backend.dao.ModificationMapper;
import org.jc.backend.dao.PurchaseOrderMapper;
import org.jc.backend.entity.DO.ModificationDO;
import org.jc.backend.entity.DO.PurchaseOrderEntryDO;
import org.jc.backend.entity.DO.PurchaseOrderEntryModifyDO;
import org.jc.backend.entity.PurchaseOrderProductModifyO;
import org.jc.backend.entity.VO.PurchaseOrderEntryWithProductsVO;
import org.jc.backend.entity.PurchaseOrderProductO;
import org.jc.backend.entity.VO.PurchaseOrderModifyVO;
import org.jc.backend.service.PurchaseOrderService;
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
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);

    private final PurchaseOrderMapper purchaseOrderMapper;
    private final ModificationMapper modificationMapper;

    public PurchaseOrderServiceImpl(PurchaseOrderMapper purchaseOrderMapper, ModificationMapper modificationMapper) {
        this.purchaseOrderMapper = purchaseOrderMapper;
        this.modificationMapper = modificationMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    public void createOrder(PurchaseOrderEntryWithProductsVO entryWithProducts) throws GlobalException {
        PurchaseOrderEntryDO newEntry = new PurchaseOrderEntryDO();
        BeanUtils.copyProperties(entryWithProducts, newEntry);
        List<PurchaseOrderProductO> newProducts = entryWithProducts.getPurchaseOrderProducts();

        // calculate the number of entries have been created for today's date, and generate new serial
        int count = purchaseOrderMapper.countNumberOfEntriesOfToday();
        String newSerial = MyUtils.formNewSerial("采订", count);

        newEntry.setPurchaseOrderEntryID(newSerial);
        try {
            purchaseOrderMapper.insertNewOrderEntry(newEntry);
        } catch (PersistenceException e) {
            e.printStackTrace();
            logger.error("Insert new purchase order failed");
            throw new GlobalException("Insert new purchase order failed");
        }

        for (var product : newProducts) {
            product.setPurchaseOrderEntryID(newSerial);
            try {
                int id = purchaseOrderMapper.insertNewOrderProduct(product);
                logger.info("Insert new purchase product id: " + id);
            } catch (PersistenceException e) {
                e.printStackTrace();
                logger.error("Insert new purchase order product failed");
                throw new GlobalException("Insert new purchase order product failed");
            }
        }
    }

    public List<PurchaseOrderEntryWithProductsVO> getOrdersInDateRangeByCompanyID(Date startDate, Date endDate, int id) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<PurchaseOrderEntryDO> entriesFromDatabase = purchaseOrderMapper.queryEntriesInDateRangeByCompanyID(
                dateFormat.format(startDate), dateFormat.format(endDate), id);

        List<PurchaseOrderEntryWithProductsVO> entries = new ArrayList<>();
        for (var entryFromDatabase : entriesFromDatabase) {
            PurchaseOrderEntryWithProductsVO tempEntry = new PurchaseOrderEntryWithProductsVO();
            BeanUtils.copyProperties(entryFromDatabase, tempEntry);

            try {
                List<PurchaseOrderProductO> products = purchaseOrderMapper.queryProductsByEntryID(
                        tempEntry.getPurchaseOrderEntryID());
                tempEntry.setPurchaseOrderProducts(products);
            } catch (PersistenceException e) {
                logger.error("Query error");
            }

            entries.add(tempEntry);
        }

        return entries;
    }

    public void modifyOrder(PurchaseOrderModifyVO modificationVO) {
        //extract entryDO
        PurchaseOrderEntryModifyDO currentEntry = new PurchaseOrderEntryModifyDO();
        BeanUtils.copyProperties(modificationVO, currentEntry);

        //extract List<productO>
        List<PurchaseOrderProductModifyO> currentProducts = modificationVO.getPurchaseOrderProducts();

        //query database for compare
        String id = currentEntry.getPurchaseOrderEntryID();
        logger.info("Serial to be changed: " + id);
        PurchaseOrderEntryModifyDO originEntry;
        List<PurchaseOrderProductModifyO> originProducts;
        try {
            originEntry = (purchaseOrderMapper.selectEntryForCompare(id)).get(0);
            originProducts = purchaseOrderMapper.selectProductsForCompare(id);
        } catch (PersistenceException e) {
            e.printStackTrace();
            return;
        }

        //compare entry
        StringBuilder record = new StringBuilder("修改者: " + currentEntry.getDrawer() + "; "); //modification_record
        boolean bool1 = MyUtils.entryCompareAndFormModificationRecord(record, currentEntry, originEntry);

        if (bool1) {
            try {
                logger.info(currentEntry.toString());
                purchaseOrderMapper.updateOrderEntry(currentEntry);
            } catch (PersistenceException e) {
                e.printStackTrace();
                //todo
            }
        }

        boolean bool2 = false; //bool to indicate changes to products
        for (var originProduct : originProducts) {
            boolean found = false;
            for (var currentProduct : currentProducts) {
                if (currentProduct.getPurchaseOrderProductID() == originProduct.getPurchaseOrderProductID()) {
                    boolean bool3 = MyUtils.productsCompareAndFormModificationRecord(
                            record, currentProduct, originProduct);

                    if (bool3) {
                        bool2 = true; //bool2 here in case only one product is changed and bool2 will be overwritten
                        try {
                            logger.info(currentProduct.toString());
                            purchaseOrderMapper.updateOrderProduct(currentProduct);
                        } catch (PersistenceException e) {
                            e.printStackTrace();
                            //todo
                        }
                    }
                    found = true;
                    break;
                }
            }
            if (!found) { //entry is removed
                try {
                    purchaseOrderMapper.deleteOrderProductByID(originProduct.getPurchaseOrderProductID());
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
                        0, originEntry.getPurchaseOrderEntryID(), record.toString(),
                        new SimpleDateFormat("yyyy-MM-dd").format(new Date())
                ));
            } catch (PersistenceException e) {
                e.printStackTrace();
                //todo
            }
        }

    }

    public void deleteOrder(String id) {
        try {
            purchaseOrderMapper.deleteOrderProductsByEntryID(id);
            purchaseOrderMapper.deleteOrderEntry(id);
        } catch (PersistenceException e) {
            logger.error("");
            e.printStackTrace();
        }
    }
}
