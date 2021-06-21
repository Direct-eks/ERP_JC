package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.ModificationMapper;
import org.jc.backend.dao.PurchaseOrderMapper;
import org.jc.backend.dao.WarehouseStockMapper;
import org.jc.backend.entity.DO.PurchaseOrderEntryDO;
import org.jc.backend.entity.ModificationO;
import org.jc.backend.entity.PurchaseOrderProductO;
import org.jc.backend.entity.VO.PurchaseOrderEntryWithProductsVO;
import org.jc.backend.entity.WarehouseStockO;
import org.jc.backend.service.PurchaseOrderService;
import org.jc.backend.utils.IOModificationUtils;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);

    private final PurchaseOrderMapper purchaseOrderMapper;
    private final ModificationMapper modificationMapper;
    private final WarehouseStockMapper warehouseStockMapper;

    public PurchaseOrderServiceImpl(PurchaseOrderMapper purchaseOrderMapper,
                                    ModificationMapper modificationMapper,
                                    WarehouseStockMapper warehouseStockMapper) {
        this.purchaseOrderMapper = purchaseOrderMapper;
        this.modificationMapper = modificationMapper;
        this.warehouseStockMapper = warehouseStockMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    @Override
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

                //check warehouseStock for existence, if not, create new one
                int warehouseID = product.getWarehouseID();
                int skuID = product.getSkuID();
                if (product.getWarehouseStockID() == -1 ||
                        warehouseStockMapper.queryWarehouseStockByWarehouseAndSku(warehouseID, skuID) == null) {
                    WarehouseStockO newWarehouseStock = new WarehouseStockO();
                    newWarehouseStock.setSkuID(skuID);
                    newWarehouseStock.setWarehouseID(warehouseID);
                    warehouseStockMapper.insertNewWarehouseStock(newWarehouseStock);
                    int newID = newWarehouseStock.getWarehouseStockID();
                    product.setWarehouseStockID(newID);
                }

                purchaseOrderMapper.insertNewOrderProduct(product);
                int id = product.getPurchaseOrderProductID();
                logger.info("Insert new purchase product id: " + id);
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Insert failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<PurchaseOrderEntryWithProductsVO> getOrdersInDateRangeByCompanyID(
            Date startDate, Date endDate, int companyID) {
        try {
            List<PurchaseOrderEntryDO> entriesFromDatabase = purchaseOrderMapper.queryEntriesInDateRangeByCompanyID(
                    MyUtils.dateFormat.format(startDate), MyUtils.dateFormat.format(endDate), companyID);

            List<PurchaseOrderEntryWithProductsVO> entries = new ArrayList<>();
            for (var entryFromDatabase : entriesFromDatabase) {
                PurchaseOrderEntryWithProductsVO tempEntry = new PurchaseOrderEntryWithProductsVO();
                BeanUtils.copyProperties(entryFromDatabase, tempEntry);

                List<PurchaseOrderProductO> products = purchaseOrderMapper.queryProductsByEntryID(
                        tempEntry.getPurchaseOrderEntryID());
                tempEntry.setPurchaseOrderProducts(products);

                entries.add(tempEntry);
            }
            return entries;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query error");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<PurchaseOrderEntryWithProductsVO> getOrdersByCompanyID(int id) {
        try {
            List<PurchaseOrderEntryDO> entriesFromDatabase = purchaseOrderMapper.queryEntriesByCompanyID(id);

            List<PurchaseOrderEntryWithProductsVO> entries = new ArrayList<>();
            for (var entryFromDatabase : entriesFromDatabase) {
                PurchaseOrderEntryWithProductsVO tempEntry = new PurchaseOrderEntryWithProductsVO();
                BeanUtils.copyProperties(entryFromDatabase, tempEntry);

                List<PurchaseOrderProductO> products = purchaseOrderMapper.queryProductsByEntryID(
                        tempEntry.getPurchaseOrderEntryID());
                tempEntry.setPurchaseOrderProducts(products);

                entries.add(tempEntry);
            }
            return entries;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query error");
            throw e;
        }
    }

    @Transactional
    @Override
    public void modifyOrder(PurchaseOrderEntryWithProductsVO purchaseOrderEntryWithProductsVO) {
        //extract entryDO
        PurchaseOrderEntryDO currentEntry = new PurchaseOrderEntryDO();
        BeanUtils.copyProperties(purchaseOrderEntryWithProductsVO, currentEntry);

        //extract List<productO>
        List<PurchaseOrderProductO> currentProducts = purchaseOrderEntryWithProductsVO.getPurchaseOrderProducts();

        try {
            //query database for compare
            String id = currentEntry.getPurchaseOrderEntryID();
            logger.info("Serial to be changed: " + id);

            PurchaseOrderEntryDO originEntry = purchaseOrderMapper.selectEntryForCompare(id);
            List<PurchaseOrderProductO> originProducts = purchaseOrderMapper.selectProductsForCompare(id);

            //first check if warehouse is changed, if so, check warehouse_stock and update all products
            if (currentEntry.getWarehouseID() != originEntry.getWarehouseID()) {
                for (var product : currentProducts) {
                    WarehouseStockO warehouseStock = warehouseStockMapper.queryWarehouseStockByWarehouseAndSku(
                            product.getWarehouseID(), product.getSkuID());
                    int newWarehouseStockID;
                    if (warehouseStock == null) {
                        WarehouseStockO newWarehouseStock = new WarehouseStockO();
                        newWarehouseStock.setSkuID(product.getSkuID());
                        newWarehouseStock.setWarehouseID(currentEntry.getWarehouseID());
                        warehouseStockMapper.insertNewWarehouseStock(newWarehouseStock);
                        newWarehouseStockID = newWarehouseStock.getWarehouseStockID();
                    }
                    else {
                        newWarehouseStockID = warehouseStock.getWarehouseStockID();
                    }
                    product.setWarehouseID(currentEntry.getWarehouseID());
                    product.setWarehouseStockID(newWarehouseStockID);
                }
            }

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
                        boolean bool3 = IOModificationUtils.productCompareAndFormModificationRecord(
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
            else {
                logger.warn("Nothing changed, begin rolling back");
                throw new RuntimeException();
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Update error");
            throw e;
        }
    }

    @Transactional
    @Override
    public void deleteOrder(String id) {
        try {
            purchaseOrderMapper.deleteOrderProductsByEntryID(id);
            purchaseOrderMapper.deleteOrderEntry(id);
        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Delete error");
            throw e;
        }
    }

}
