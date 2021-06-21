package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.ModificationMapper;
import org.jc.backend.dao.SalesOrderMapper;
import org.jc.backend.dao.WarehouseStockMapper;
import org.jc.backend.entity.DO.SalesOrderEntryDO;
import org.jc.backend.entity.ModificationO;
import org.jc.backend.entity.SalesOrderProductO;
import org.jc.backend.entity.VO.SalesOrderEntryWithProductsVO;
import org.jc.backend.entity.WarehouseStockO;
import org.jc.backend.service.SalesOrderService;
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
public class SalesOrderServiceImpl implements SalesOrderService {
    private static final Logger logger = LoggerFactory.getLogger(SalesOrderServiceImpl.class);

    private final SalesOrderMapper salesOrderMapper;
    private final ModificationMapper modificationMapper;
    private final WarehouseStockMapper warehouseStockMapper;

    public SalesOrderServiceImpl(SalesOrderMapper salesOrderMapper,
                                 ModificationMapper modificationMapper,
                                 WarehouseStockMapper warehouseStockMapper) {
        this.salesOrderMapper = salesOrderMapper;
        this.modificationMapper = modificationMapper;
        this.warehouseStockMapper = warehouseStockMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    @Override
    public void createOrder(SalesOrderEntryWithProductsVO salesOrderEntryWithProductsVO) {

        SalesOrderEntryDO newEntry = new SalesOrderEntryDO();
        BeanUtils.copyProperties(salesOrderEntryWithProductsVO, newEntry);
        List<SalesOrderProductO> newProducts = salesOrderEntryWithProductsVO.getSalesOrderProducts();

        try {
            int count = salesOrderMapper.countNumberOfEntriesOfToday();
            String newSerial = MyUtils.formNewSerial("销订", count, newEntry.getShipmentDate());

            newEntry.setSalesOrderEntryID(newSerial);
            salesOrderMapper.insertNewOrderEntry(newEntry);

            for (var product : newProducts) {
                product.setSalesOrderEntryID(newSerial);
                salesOrderMapper.insertNewOrderProduct(product);
                int id = product.getSalesOrderProductID();
                logger.info("Insert new sales product id: " + id);
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Insert failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<SalesOrderEntryWithProductsVO> getOrdersInDateRangeByCompanyID(Date startDate, Date endDate, int id) {
        try {
            List<SalesOrderEntryDO> entriesFromDatabase = salesOrderMapper.queryEntriesInDateRangeByCompanyID(
                    MyUtils.dateFormat.format(startDate), MyUtils.dateFormat.format(endDate), id);

            List<SalesOrderEntryWithProductsVO> entries = new ArrayList<>();
            for (var entryFromDatabase : entriesFromDatabase) {
                SalesOrderEntryWithProductsVO tempEntry = new SalesOrderEntryWithProductsVO();
                BeanUtils.copyProperties(entryFromDatabase, tempEntry);

                List<SalesOrderProductO> products = salesOrderMapper.queryProductsByEntryID(
                        tempEntry.getSalesOrderEntryID());
                tempEntry.setSalesOrderProducts(products);

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
    public List<SalesOrderEntryWithProductsVO> getOrdersByCompanyID(int id) {
        try {
            List<SalesOrderEntryDO> entriesFromDatabase = salesOrderMapper.queryEntriesByCompanyID(id);

            List<SalesOrderEntryWithProductsVO> entries = new ArrayList<>();
            for (var entryFromDatabase : entriesFromDatabase) {
                SalesOrderEntryWithProductsVO tempEntry = new SalesOrderEntryWithProductsVO();
                BeanUtils.copyProperties(entryFromDatabase, tempEntry);

                List<SalesOrderProductO> products = salesOrderMapper.queryProductsByEntryID(
                        tempEntry.getSalesOrderEntryID());
                tempEntry.setSalesOrderProducts(products);

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
    public void modifyOrder(SalesOrderEntryWithProductsVO salesOrderEntryWithProductsVO) {

        SalesOrderEntryDO currentEntry = new SalesOrderEntryDO();
        BeanUtils.copyProperties(salesOrderEntryWithProductsVO, currentEntry);
        List<SalesOrderProductO> currentProducts = salesOrderEntryWithProductsVO.getSalesOrderProducts();

        try {
            //query database for compare
            String id = currentEntry.getSalesOrderEntryID();
            logger.info("Serial to be changed: " + id);

            SalesOrderEntryDO originEntry = salesOrderMapper.selectEntryForCompare(id);
            List<SalesOrderProductO> originProducts = salesOrderMapper.selectProductsForCompare(id);

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

            StringBuilder record = new StringBuilder("修改者: " + currentEntry.getDrawer() + "; "); //modification_record
            boolean bool1 = IOModificationUtils.entryCompareAndFormModificationRecord(record, currentEntry, originEntry);

            if (bool1) {
                salesOrderMapper.updateOrderEntry(currentEntry);
            }

            boolean bool2 = false; //bool to indicate changes to products
            for (var originProduct : originProducts) {
                boolean found = false;
                for (var currentProduct : currentProducts) {
                    if (currentProduct.getSalesOrderProductID() == originProduct.getSalesOrderProductID()) {
                        boolean bool3 = IOModificationUtils.productCompareAndFormModificationRecord(
                                record, currentProduct, originProduct);

                        if (bool3) {
                            bool2 = true; //bool2 here in case only one product is changed and bool2 will be overwritten
                            salesOrderMapper.updateOrderProduct(currentProduct);
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) { //entry is removed
                    salesOrderMapper.deleteOrderProductByID(originProduct.getSalesOrderProductID());
                }
            }

            if (bool1 || bool2) {
                logger.info("Modification: " + record);
                modificationMapper.insertModificationRecord(new ModificationO(
                        originEntry.getSalesOrderEntryID(), record.toString()));
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
            salesOrderMapper.deleteOrderProductsByEntryID(id);
            salesOrderMapper.deleteOrderEntry(id);
        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query error");
            throw e;
        }
    }

}
