package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.config.exception.GlobalException;
import org.jc.backend.dao.ModificationMapper;
import org.jc.backend.dao.PurchaseOrderMapper;
import org.jc.backend.entity.DO.ModificationDO;
import org.jc.backend.entity.DO.PurchaseOrderEntryDO;
import org.jc.backend.entity.DO.PurchaseOrderEntryModifyDO;
import org.jc.backend.entity.DO.PurchaseOrderProductModifyDO;
import org.jc.backend.entity.VO.PurchaseOrderEntryWithProductsVO;
import org.jc.backend.entity.PurchaseOrderProductO;
import org.jc.backend.entity.VO.PurchaseOrderModifyVO;
import org.jc.backend.service.PurchaseOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    public void createPurchaseOrder(PurchaseOrderEntryWithProductsVO entryWithProducts) throws GlobalException {
        PurchaseOrderEntryDO newEntry = new PurchaseOrderEntryDO();
        BeanUtils.copyProperties(entryWithProducts, newEntry);
        List<PurchaseOrderProductO> newProducts = entryWithProducts.getPurchaseOrderProducts();

        // calculate the number of entries have been created for today's date, and generate new serial
        int count = purchaseOrderMapper.countNumberOfEntries();
        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        dateString = dateString.substring(2).replaceAll("-", "");
        String newSerial = String.format("采订%s-%03d", dateString, count + 1);
        logger.info("New serial: " + newSerial);

        newEntry.setPurchaseOrderEntryID(newSerial);
        try {
            purchaseOrderMapper.insertNewEntry(newEntry);
        } catch (PersistenceException e) {
            e.printStackTrace();
            logger.error("Insert new purchase order failed");
            throw new GlobalException("Insert new purchase order failed");
        }

        for (var product : newProducts) {
            product.setPurchaseOrderEntryID(newSerial);
            try {
                int id = purchaseOrderMapper.insertNewProduct(product);
                logger.info("Insert new purchase product id: " + id);
            } catch (PersistenceException e) {
                e.printStackTrace();
                logger.error("Insert new purchase order product failed");
                throw new GlobalException("Insert new purchase order product failed");
            }
        }
    }

    public List<PurchaseOrderEntryWithProductsVO> getOrdersWithinDateRangeByCompanyID(Date startDate, Date endDate, int id) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<PurchaseOrderEntryDO> entriesFromDatabase = purchaseOrderMapper.queryEntriesWithinDateRangeByCompanyID(
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

    public void modifyPurchaseOrder(PurchaseOrderModifyVO modificationVO) {
        //extract entryDO
        PurchaseOrderEntryModifyDO currentEntry = new PurchaseOrderEntryModifyDO();
        BeanUtils.copyProperties(modificationVO, currentEntry);

        //extract List<productDO>
        List<PurchaseOrderProductModifyDO> currentProducts = new ArrayList<>();
        modificationVO.getPurchaseOrderProducts().forEach(productVO -> {
            PurchaseOrderProductModifyDO tempProductDO = new PurchaseOrderProductModifyDO();
            BeanUtils.copyProperties(productVO, tempProductDO);
            currentProducts.add(tempProductDO);
        });

        //query database for compare
        String id = currentEntry.getPurchaseOrderEntryID();
        logger.info("Serial to be changed: " + id);
        PurchaseOrderEntryModifyDO originEntry;
        List<PurchaseOrderProductModifyDO> originProducts;
        try {
            originEntry = (purchaseOrderMapper.selectEntryForCompare(id)).get(0);
            originProducts = purchaseOrderMapper.selectProductsForCompare(id);
        } catch (PersistenceException e) {
            e.printStackTrace();
            return;
        }

        //compare entry
        StringBuilder record = new StringBuilder("修改者: " + currentEntry.getDrawer() + "; "); //modification_record
        boolean bool1; //bool to indicate changes to entry
        currentEntry.setTotalCostChange(bool1 = (currentEntry.getTotalCost() != originEntry.getTotalCost()));
        if (bool1) record.append(String.format("总金额: %f -> %f; ", originEntry.getTotalCost(),
                currentEntry.getTotalCost()));
        currentEntry.setInvoiceTypeChange(bool1 =
                (!currentEntry.getInvoiceType().equals(originEntry.getInvoiceType())));
        if (bool1) record.append(String.format("单据类型: %s -> %s; ", originEntry.getInvoiceType(),
                currentEntry.getInvoiceType()));
        currentEntry.setExecutionStatusChange(bool1 =
                (!currentEntry.getExecutionStatus().equals(originEntry.getExecutionStatus())));
        if (bool1) record.append(String.format("状态: %s -> %s; ", originEntry.getExecutionStatus(),
                currentEntry.getExecutionStatus()));
        currentEntry.setDepartmentIDChange(bool1 = (currentEntry.getDepartmentID() != originEntry.getDepartmentID()));
        if (bool1) record.append(String.format("部门: %s -> %s; ", originEntry.getDepartmentName(),
                currentEntry.getDepartmentName()));
        currentEntry.setWarehouseIDChange(bool1 = (currentEntry.getWarehouseID() != originEntry.getWarehouseID()));
        if (bool1) record.append(String.format("仓库: %s -> %s; ", originEntry.getWarehouseName(),
                currentEntry.getWarehouseName()));
        currentEntry.setRemarkChange(bool1 = (!currentEntry.getRemark().equals(originEntry.getRemark())));
        if (bool1) record.append(String.format("备注: %s -> %s; ", originEntry.getRemark(), currentEntry.getRemark()));

        if (bool1) {
            try {
                logger.info(currentEntry.toString());
                purchaseOrderMapper.updatePurchaseOrderEntry(currentEntry);
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
                    String modelCode = StringUtils.hasLength(currentProduct.getNewCode()) ?
                            currentProduct.getNewCode() : currentProduct.getOldCode();
                    // compare product
                    boolean bool3;
                    currentProduct.setQuantityChange(bool3 =
                            (currentProduct.getQuantity() != originProduct.getQuantity()));
                    if (bool3) record.append(String.format("%s 数量: %d -> %d; ", modelCode, originProduct.getQuantity(),
                            originProduct.getQuantity()));
                    currentProduct.setRemarkChange(bool3 =
                            (!currentProduct.getRemark().equals(originProduct.getRemark())));
                    if (bool3) record.append(String.format("%s 备注: %s -> %s; ", modelCode, originProduct.getRemark(),
                            currentProduct.getRemark()));
                    currentProduct.setTaxRateChange(bool3 =
                            (currentProduct.getTaxRate() != originProduct.getTaxRate()));
                    if (bool3) record.append(String.format("%s 税率: %f -> %f; ", modelCode, originProduct.getTaxRate(),
                            currentProduct.getTaxRate()));
                    currentProduct.setUnitPriceWithoutTaxChange(bool3 =
                            (currentProduct.getUnitPriceWithoutTax() != originProduct.getUnitPriceWithoutTax()));
                    if (bool3) record.append(String.format("%s 单价: %f -> %f; ", modelCode,
                            originProduct.getUnitPriceWithoutTax(), currentProduct.getUnitPriceWithoutTax()));
                    currentProduct.setUnitPriceWithTaxChange(bool3 =
                            (currentProduct.getUnitPriceWithTax() != originProduct.getUnitPriceWithTax()));
                    if (bool3) record.append(String.format("%s 税价: %f -> %f; ", modelCode,
                            originProduct.getUnitPriceWithTax(), currentProduct.getUnitPriceWithTax()));

                    if (bool3) {
                        bool2 = true; //bool2 here in case only one product is changed and bool2 will be overwritten
                        try {
                            logger.info(currentProduct.toString());
                            purchaseOrderMapper.updatePurchaseOrderProduct(currentProduct);
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
                    purchaseOrderMapper.deletePurchaseOrderProducts(originProduct.getPurchaseOrderEntryID());
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

    public void deletePurchaseOrder(String id) {
        try {
            purchaseOrderMapper.deletePurchaseOrderProducts(id);
            purchaseOrderMapper.deletePurchaseOrderEntry(id);
        } catch (PersistenceException e) {
            logger.error("");
            e.printStackTrace();
        }
    }
}
