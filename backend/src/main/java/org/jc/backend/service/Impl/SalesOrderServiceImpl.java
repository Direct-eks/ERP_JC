package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.SalesOrderMapper;
import org.jc.backend.entity.DO.SalesOrderEntryDO;
import org.jc.backend.entity.SalesOrderProductO;
import org.jc.backend.entity.StatO.SummaryO;
import org.jc.backend.entity.VO.SalesOrderEntryWithProductsVO;
import org.jc.backend.service.ModelService;
import org.jc.backend.service.ModificationRecordService;
import org.jc.backend.service.SalesOrderService;
import org.jc.backend.service.WarehouseStockService;
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
    private final ModificationRecordService modificationRecordService;
    private final WarehouseStockService warehouseStockService;
    private final ModelService modelService;

    public SalesOrderServiceImpl(SalesOrderMapper salesOrderMapper,
                                 ModificationRecordService modificationRecordService,
                                 WarehouseStockService warehouseStockService,
                                 ModelService modelService) {
        this.salesOrderMapper = salesOrderMapper;
        this.modificationRecordService = modificationRecordService;
        this.warehouseStockService = warehouseStockService;
        this.modelService = modelService;
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
            logger.info("Inserted new sales order entry: {}", newSerial);

            for (var product : newProducts) {
                product.setSalesOrderEntryID(newSerial);
                salesOrderMapper.insertNewOrderProduct(product);
                int id = product.getSalesOrderProductID();
                logger.info("Insert new sales product: {}", id);
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

            SalesOrderEntryDO originEntry = salesOrderMapper.selectEntryForCompare(id);
            List<SalesOrderProductO> originProducts = salesOrderMapper.selectProductsForCompare(id);

            // first check if warehouse is changed, if so, check warehouse_stock and update all products
            if (currentEntry.getWarehouseID() != originEntry.getWarehouseID()) {
                for (var product : currentProducts) {
                    var s = warehouseStockService.getWarehouseStockByWarehouseAndSku(
                            product.getWarehouseID(), product.getSkuID());
                    if (s == null) {
                        logger.info("Changed sales product warehouse stock ID from {} to {}",
                                product.getWarehouseStockID(), -1);
                        product.setWarehouseStockID(-1);
                    }
                    else {
                        logger.info("Changed sales product warehouse stock ID from {} to {}",
                                product.getWarehouseStockID(), s.getWarehouseStockID());
                        product.setWarehouseStockID(s.getWarehouseStockID());
                    }
                    product.setWarehouseID(currentEntry.getWarehouseID());
                }
            }

            StringBuilder record = new StringBuilder("修改者: " + currentEntry.getDrawer() + "; ");
            boolean entryChanged = IOModificationUtils.entryCompareAndFormModificationRecord(record, currentEntry, originEntry);

            if (entryChanged) {
                salesOrderMapper.updateOrderEntry(currentEntry);
                logger.info("Updated sales order: {}", id);
            }

            boolean productsChanged = false; //bool to indicate changes to products
            for (var originProduct : originProducts) {
                boolean found = false;
                for (var currentProduct : currentProducts) {
                    if (currentProduct.getSalesOrderProductID() == originProduct.getSalesOrderProductID()) {
                        boolean productChanged = IOModificationUtils.productCompareAndFormModificationRecord(
                                record, currentProduct, originProduct);

                        if (productChanged) {
                            productsChanged = true; //productsChanged here in case only one product is changed and productsChanged will be overwritten
                            salesOrderMapper.updateOrderProduct(currentProduct);
                            logger.info("Updated sales product: {}", originProduct.getSalesOrderProductID());
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) { // product is removed
                    salesOrderMapper.deleteOrderProductByID(originProduct.getSalesOrderProductID());
                    logger.info("Deleted sales product: {}", originProduct.getSalesOrderProductID());
                }
            }

            if (entryChanged || productsChanged) {
                modificationRecordService.insertRecord(originEntry.getSalesOrderEntryID(), record);
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
            logger.info("Deleted sales entry: {}", id);
            salesOrderMapper.deleteOrderEntry(id);
            logger.info("Deleted sales product with serial {}", id);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query error");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<SummaryO> getSalesSummary(int companyID, String startDate, String endDate, int categoryID,
                                          String factoryBrand, int warehouseID, int departmentID) {
        try {
            String treeLevel = modelService.getTreeLevelByCategoryID(categoryID);
            factoryBrand = factoryBrand.isBlank() ? "" : factoryBrand;

            var list = salesOrderMapper.querySummary(companyID, startDate, endDate,
                    treeLevel, factoryBrand, warehouseID, departmentID);
            list.forEach(item -> {
                double unitPriceWithTax = Double.parseDouble(item.getUnitPriceWithTax());
                item.setTotalPrice(Double.toString(unitPriceWithTax * item.getQuantity()));
            });
            return list;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query error");
            throw e;
        }
    }
}
