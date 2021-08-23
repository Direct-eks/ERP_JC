package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.WarehouseInEntryMapper;
import org.jc.backend.dao.WarehouseOutEntryMapper;
import org.jc.backend.entity.DO.WarehouseEntryDO;
import org.jc.backend.entity.ModificationO;
import org.jc.backend.entity.StatO.ProductStatO;
import org.jc.backend.entity.StatO.SummaryO;
import org.jc.backend.entity.VO.WarehouseEntryWithProductsVO;
import org.jc.backend.entity.WarehouseProductO;
import org.jc.backend.entity.WarehouseStockO;
import org.jc.backend.service.ModelService;
import org.jc.backend.service.ModificationRecordService;
import org.jc.backend.service.WarehouseEntryService;
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
public class WarehouseEntryServiceImpl implements WarehouseEntryService {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseEntryServiceImpl.class);

    private final WarehouseInEntryMapper warehouseInEntryMapper;
    private final WarehouseOutEntryMapper warehouseOutEntryMapper;
    private final WarehouseStockService warehouseStockService;
    private final ModificationRecordService modificationRecordService;
    private final ModelService modelService;

    public WarehouseEntryServiceImpl(
            WarehouseInEntryMapper warehouseInEntryMapper,
            WarehouseOutEntryMapper warehouseOutEntryMapper,
            WarehouseStockService warehouseStockService,
            ModificationRecordService modificationRecordService,
            ModelService modelService) {
        this.warehouseInEntryMapper = warehouseInEntryMapper;
        this.warehouseOutEntryMapper = warehouseOutEntryMapper;
        this.warehouseStockService = warehouseStockService;
        this.modificationRecordService = modificationRecordService;
        this.modelService = modelService;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    @Override
    public void createEntry(WarehouseEntryWithProductsVO entryVO, String type, boolean isInbound) {

        WarehouseEntryDO newEntry = new WarehouseEntryDO();
        BeanUtils.copyProperties(entryVO, newEntry);
        List<WarehouseProductO> newProducts = entryVO.getEntryProducts();

        try {
            // todo presale check

            String entryDate = newEntry.getEntryDate();
            int count = isInbound ? warehouseInEntryMapper.countNumberOfEntriesOfGivenDateAndType(entryDate, type) :
                    warehouseOutEntryMapper.countNumberOfEntriesOfGivenDateAndType(entryDate, type);
            String newSerial = MyUtils.formNewSerial(type, count, entryDate);

            newEntry.setWarehouseEntryID(newSerial);
            if (isInbound) {
                warehouseInEntryMapper.insertNewEntry(newEntry);
            } else {
                warehouseOutEntryMapper.insertNewEntry(newEntry);
            }

            int warehouseID = newEntry.getWarehouseID();
            for (var product : newProducts) {
                product.setWarehouseEntryID(newSerial);

                int skuID = product.getSkuID();
                if (product.getWarehouseStockID() == -1 ||
                        warehouseStockService.getWarehouseStockByWarehouseAndSku(warehouseID, skuID) == null) {
                    if (isInbound) {
                        WarehouseStockO newWarehouseStock = new WarehouseStockO();
                        newWarehouseStock.setSkuID(skuID);
                        newWarehouseStock.setWarehouseID(warehouseID);
                        int newID = warehouseStockService.insertNewWarehouseStock(newWarehouseStock);
                        product.setWarehouseStockID(newID);
                        logger.info("Insert new warehouse product id: {}", newID);
                    }
                    else {
                        // todo outbound does not support on product with no stock record
                        logger.warn("Outbound does not support on product with no stock record");
                        throw new RuntimeException(); // not implemented or handled
                    }
                }

                if (isInbound) {
                    warehouseStockService.increaseStock(product, entryDate, type);
                    warehouseInEntryMapper.insertNewProduct(product);
                    logger.info("Inserted warehouse in entry, {}", product.getWarehouseEntryID());
                }
                else {
                    warehouseStockService.decreaseStock(product, entryDate, type);
                    warehouseOutEntryMapper.insertNewProduct(product);
                    logger.info("Inserted warehouse out entry, {}", product.getWarehouseEntryID());
                }
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("insert failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<WarehouseEntryWithProductsVO> getEntriesInDateRange(Date startDate, Date endDate,
                                                                    String type, boolean isInbound) {
        try {
            List<WarehouseEntryDO> entriesFromDatabase;
            if (isInbound) {
                entriesFromDatabase = warehouseInEntryMapper.queryEntriesInDateRangeByType(
                        MyUtils.dateFormat.format(startDate), MyUtils.dateFormat.format(endDate), type);
            }
            else {
                entriesFromDatabase = warehouseOutEntryMapper.queryEntriesInDateRangeByType(
                        MyUtils.dateFormat.format(startDate), MyUtils.dateFormat.format(endDate), type);
            }

            List<WarehouseEntryWithProductsVO> entries = new ArrayList<>();
            for (var entry : entriesFromDatabase) {
                WarehouseEntryWithProductsVO tempEntry = new WarehouseEntryWithProductsVO();
                BeanUtils.copyProperties(entry, tempEntry);

                List<WarehouseProductO> products;
                if (isInbound) {
                    products = warehouseInEntryMapper.queryProductsByEntryID(tempEntry.getWarehouseEntryID());
                }
                else {
                    products = warehouseOutEntryMapper.queryProductsByEntryID(tempEntry.getWarehouseEntryID());
                }
                tempEntry.setEntryProducts(products);

                entries.add(tempEntry);
            }
            return entries;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void modifyEntry(WarehouseEntryWithProductsVO entryVO, String type, boolean isInbound) {

        WarehouseEntryDO currentEntry = new WarehouseEntryDO();
        BeanUtils.copyProperties(entryVO, currentEntry);
        List<WarehouseProductO> currentProducts = entryVO.getEntryProducts();

        try {
            String id = currentEntry.getWarehouseEntryID();

            WarehouseEntryDO originalEntry;
            originalEntry = isInbound ? warehouseInEntryMapper.selectEntryForCompare(id) :
                    warehouseOutEntryMapper.selectEntryForCompare(id);
            List<WarehouseProductO> originalProducts;
            originalProducts = isInbound ? warehouseInEntryMapper.selectProductsForCompare(id) :
                    warehouseOutEntryMapper.selectProductsForCompare(id);

            StringBuilder record = new StringBuilder("修改者: " + currentEntry.getDrawer() + "; ");
            boolean entryChanged = false;
            boolean productsChanged = false;
            if (IOModificationUtils.warehouseEntryCompareAndFormModificationRecord(
                    record, currentEntry, originalEntry, isInbound)) {
                entryChanged = true;
                if (isInbound) {
                    warehouseInEntryMapper.updateEntry(currentEntry);
                    logger.info("Updated warehouse in entry, {}", id);
                }
                else {
                    warehouseOutEntryMapper.updateEntry(currentEntry);
                    logger.info("Updated warehouse out entry, {}", id);
                }
            }

            for (var originalProduct : originalProducts) {
                boolean found = false;
                for (var currentProduct : currentProducts) {
                    if (currentProduct.getWarehouseProductID() == originalProduct.getWarehouseProductID()) {
                        if (IOModificationUtils.warehouseProductCompareAndFormModificationRecord(
                                record, currentProduct, originalProduct, isInbound)) {
                            productsChanged = true;
                            warehouseStockService.modifyStock(currentProduct, currentEntry.getEntryDate(), isInbound);
                            if (isInbound) {
                                warehouseInEntryMapper.updateProduct(currentProduct);
                                logger.info("Updated warehouse in entry product, {}",
                                        currentProduct.getWarehouseProductID());
                            }
                            else {
                                warehouseOutEntryMapper.updateProduct(currentProduct);
                                logger.info("Updated warehouse out entry product, {}",
                                        currentProduct.getWarehouseProductID());
                            }
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    // todo adapt to deletion
                    logger.error("deleted product found");
                    throw new RuntimeException();
                }
            }

            if (entryChanged || productsChanged) {
                modificationRecordService.insertRecord(originalEntry.getWarehouseEntryID(), record);
            }
            else {
                logger.info("Nothing changed, begin rolling back");
                throw new RuntimeException();
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductStatO> getAllProductsByWarehouseStockID(int id, boolean isInbound) {
        try {
            return isInbound ? warehouseInEntryMapper.queryAllInboundProductsByWarehouseStockID(id) :
                    warehouseOutEntryMapper.queryAllOutboundProductsByWarehouseStockID(id);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateProductForStock(ProductStatO productStatO, boolean isInbound) {
        try {
            if (isInbound) {
                warehouseInEntryMapper.updateProductStockInfo(productStatO);
                logger.info("Updated warehouse in product stock info, {}", productStatO.getInboundProductID());
            } else {
                warehouseOutEntryMapper.updateProductStockInfo(productStatO);
                logger.info("Updated warehouse out product stock info, {}", productStatO.getOutboundProductID());
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<SummaryO> getSummary(boolean isInbound, String type, int companyID, String startDate, String endDate,
                                     int categoryID, String factoryBrand, int warehouseID, int departmentID) {
        try {
            if (companyID != -1 && companyID != 0) return new ArrayList<>();
            String treeLevel = modelService.getTreeLevelByCategoryID(categoryID);
            factoryBrand = factoryBrand.isBlank() ? "" : factoryBrand;

            List<SummaryO> list;
            if (isInbound) {
                list = warehouseInEntryMapper.querySummary(treeLevel, type, startDate, endDate,
                        factoryBrand, warehouseID, departmentID);
            }
            else {
                list = warehouseOutEntryMapper.querySummary(treeLevel, type, startDate, endDate,
                        factoryBrand, warehouseID, departmentID);
            }
            list.forEach(item -> {
                double unitPriceWithoutTax = Double.parseDouble(item.getUnitPriceWithoutTax());
                item.setTotalPrice(Double.toString(unitPriceWithoutTax * item.getQuantity()));
            });
            return list;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }
}
