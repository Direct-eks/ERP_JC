package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.ModificationMapper;
import org.jc.backend.dao.PurchaseOrderMapper;
import org.jc.backend.entity.DO.PurchaseOrderEntryDO;
import org.jc.backend.entity.ModificationO;
import org.jc.backend.entity.PurchaseOrderProductO;
import org.jc.backend.entity.StatO.SummaryO;
import org.jc.backend.entity.VO.PurchaseOrderEntryWithProductsVO;
import org.jc.backend.service.ModelService;
import org.jc.backend.service.PurchaseOrderService;
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
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);

    private final PurchaseOrderMapper purchaseOrderMapper;
    private final ModificationMapper modificationMapper;
    private final WarehouseStockService warehouseStockService;
    private final ModelService modelService;

    public PurchaseOrderServiceImpl(PurchaseOrderMapper purchaseOrderMapper,
                                    ModificationMapper modificationMapper,
                                    WarehouseStockService warehouseStockService,
                                    ModelService modelService) {
        this.purchaseOrderMapper = purchaseOrderMapper;
        this.modificationMapper = modificationMapper;
        this.warehouseStockService = warehouseStockService;
        this.modelService = modelService;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    @Override
    public void createOrder(PurchaseOrderEntryWithProductsVO entryWithProducts) {

        PurchaseOrderEntryDO newEntry = new PurchaseOrderEntryDO();
        BeanUtils.copyProperties(entryWithProducts, newEntry);
        List<PurchaseOrderProductO> newProducts = entryWithProducts.getPurchaseOrderProducts();

        try {
            // calculate the number of entries have been created for entry date, and generate new serial
            int count = purchaseOrderMapper.countNumberOfEntriesOfToday(newEntry.getEntryDate());
            String newSerial = MyUtils.formNewSerial("采订", count, newEntry.getEntryDate());

            newEntry.setPurchaseOrderEntryID(newSerial);
            purchaseOrderMapper.insertNewOrderEntry(newEntry);

            for (var product : newProducts) {
                product.setPurchaseOrderEntryID(newSerial);

                //check warehouseStock for existence, if not, create new one
                int warehouseID = product.getWarehouseID();
                int skuID = product.getSkuID();
                if (warehouseStockService.getWarehouseStockByWarehouseAndSku(warehouseID, skuID) == null) {
                    product.setWarehouseStockID(-1);
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
                    var s = warehouseStockService.getWarehouseStockByWarehouseAndSku(
                            product.getWarehouseID(), product.getSkuID());
                    if (s == null) {
                        product.setWarehouseStockID(-1);
                    }
                    else {
                        product.setWarehouseStockID(s.getWarehouseStockID());
                    }
                    product.setWarehouseID(currentEntry.getWarehouseID());
                }
            }

            //compare entry
            StringBuilder record = new StringBuilder("修改者: " + currentEntry.getDrawer() + "; "); //modification_record
            boolean entryChanged = IOModificationUtils.entryCompareAndFormModificationRecord(record, currentEntry, originEntry);

            if (entryChanged) {
                purchaseOrderMapper.updateOrderEntry(currentEntry);
            }

            boolean productsChanged = false;
            for (var originProduct : originProducts) {
                boolean found = false;
                for (var currentProduct : currentProducts) {
                    if (currentProduct.getPurchaseOrderProductID() == originProduct.getPurchaseOrderProductID()) {
                        boolean productChanged = IOModificationUtils.productCompareAndFormModificationRecord(
                                record, currentProduct, originProduct);

                        if (productChanged) {
                            productsChanged = true; //productsChanged here in case only one product is changed and productsChanged will be overwritten
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

            if (entryChanged || productsChanged) {
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

    @Transactional(readOnly = true)
    @Override
    public List<SummaryO> getPurchaseSummary(String startDate, String endDate, int categoryID,
                                             String factoryBrand, int warehouseID, int departmentID) {
        try {
            String treeLevel = modelService.getTreeLevelByCategoryID(categoryID);

            var list = purchaseOrderMapper.querySummary(treeLevel);
            list.removeIf(item -> {
                if (item.getEntryDate().compareTo(startDate) < 0 ||
                        item.getEntryDate().compareTo(endDate) > 0) {
                    return true;
                }
                if (!factoryBrand.isBlank() && !item.getFactoryCode().equals(factoryBrand)) {
                    return true;
                }
                if (warehouseID != -1 && item.getWarehouseID() != warehouseID) {
                    return true;
                }
                if (departmentID != -1 && item.getDepartmentID() != departmentID) {
                    return true;
                }
                return false;
            });
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
