package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.ModificationMapper;
import org.jc.backend.dao.OutboundEntryMapper;
import org.jc.backend.entity.DO.OutboundEntryDO;
import org.jc.backend.entity.ModificationO;
import org.jc.backend.entity.OutboundProductO;
import org.jc.backend.entity.StatO.InvoiceStatDO;
import org.jc.backend.entity.StatO.InvoiceStatVO;
import org.jc.backend.entity.VO.OutboundEntryWithProductsVO;
import org.jc.backend.service.OutboundEntryService;
import org.jc.backend.service.WarehouseStockService;
import org.jc.backend.utils.IOModificationUtils;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OutboundEntryServiceImpl implements OutboundEntryService {
    private static final Logger logger = LoggerFactory.getLogger(OutboundEntryServiceImpl.class);

    private final OutboundEntryMapper outboundEntryMapper;
    private final ModificationMapper modificationMapper;
    private final WarehouseStockService warehouseStockService;

    public OutboundEntryServiceImpl(OutboundEntryMapper outboundEntryMapper,
                                    ModificationMapper modificationMapper,
                                    WarehouseStockService warehouseStockService) {
        this.outboundEntryMapper = outboundEntryMapper;
        this.modificationMapper = modificationMapper;
        this.warehouseStockService = warehouseStockService;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    @Override
    public void createEntry(OutboundEntryWithProductsVO entryWithProductsVO) {

        OutboundEntryDO newEntry = new OutboundEntryDO();
        BeanUtils.copyProperties(entryWithProductsVO, newEntry);
        List<OutboundProductO> newProducts = entryWithProductsVO.getOutboundProducts();

        // set shipping cost to 0 if it is blank
        if (newEntry.getShippingCost().trim().equals("")) {
            newEntry.setShippingCost("0");
        }

        try {
            int count = outboundEntryMapper.countNumberOfEntriesOfToday();
            String newSerial = MyUtils.formNewSerial("销出", count, newEntry.getShipmentDate());

            newEntry.setOutboundEntryID(newSerial);
            outboundEntryMapper.insertNewEntry(newEntry);

            for (var product : newProducts) {
                product.setOutboundEntryID(newSerial);
                int id = outboundEntryMapper.insertNewProduct(product);
                logger.info("Insert new outbound product id: " + id);

                warehouseStockService.decreaseStock(product);
            }

            //todo mark replenish

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("insert failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<OutboundEntryWithProductsVO> getEntriesInDateRange(Date startDate, Date endDate, String type, int id) {
        try {
            List<OutboundEntryDO> entriesFromDatabase = outboundEntryMapper.queryEntriesInDateRangeByTypeAndCompanyID(
                    MyUtils.dateFormat.format(startDate), MyUtils.dateFormat.format(endDate), type, id);

            List<OutboundEntryWithProductsVO> entries = new ArrayList<>();
            for (var entryFromDatabase : entriesFromDatabase) {
                OutboundEntryWithProductsVO tempEntry = new OutboundEntryWithProductsVO();
                BeanUtils.copyProperties(entryFromDatabase, tempEntry);

                List<OutboundProductO> products = outboundEntryMapper.queryProductsByEntryID(
                        tempEntry.getOutboundEntryID());
                tempEntry.setOutboundProducts(products);

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
    public void completeEntry(OutboundEntryWithProductsVO outboundEntryWithProductsVO) {

        OutboundEntryDO currentInfo = new OutboundEntryDO();
        BeanUtils.copyProperties(outboundEntryWithProductsVO, currentInfo);

        if (currentInfo.getShippingCost().trim().equals("")) {
            currentInfo.setShippingCost("0");
        }

        try {
            String id = currentInfo.getOutboundEntryID();
            OutboundEntryDO originInfo = outboundEntryMapper.selectEntryShippingInfoForCompare(id);

            StringBuilder record = new StringBuilder("修改者: " + currentInfo.getDrawer() + "; ");
            boolean bool = IOModificationUtils.shippingInfoCompareAndFormModificationRecord(
                    record, currentInfo, originInfo);

            if (bool) {
                outboundEntryMapper.updateShippingInfo(currentInfo);

                logger.info("Completion: " + record);
                modificationMapper.insertModificationRecord(new ModificationO(
                        originInfo.getOutboundEntryID(), record.toString()));
            }
            else {
                logger.warn("Nothing changed!");
                throw new RuntimeException();
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Completion failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void modifyEntry(OutboundEntryWithProductsVO outboundEntryWithProductsVO) {

        OutboundEntryDO currentEntry = new OutboundEntryDO();
        BeanUtils.copyProperties(outboundEntryWithProductsVO, currentEntry);

        List<OutboundProductO> currentProducts = outboundEntryWithProductsVO.getOutboundProducts();

        try {
            String id = currentEntry.getOutboundEntryID();
            logger.info("Serial to be changed: " + id);

            OutboundEntryDO originEntry = outboundEntryMapper.selectEntryForCompare(id);
            List<OutboundProductO> originProducts = outboundEntryMapper.selectProductsForCompare(id);

            StringBuilder record = new StringBuilder("修改者: " + currentEntry.getDrawer() + "; ");
            boolean bool1 = IOModificationUtils.entryCompareAndFormModificationRecord(
                    record, currentEntry, originEntry);

            if (bool1) {
                outboundEntryMapper.updateEntry(currentEntry);
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
                            outboundEntryMapper.updateProduct(currentProduct);

                            //todo add warehouse stock
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    outboundEntryMapper.deleteProductByID(originProduct.getOutboundProductID());
                    //todo add warehouse stock
                }
            }

            if (bool1 || bool2) {
                logger.info("Modification: " + record);
                modificationMapper.insertModificationRecord(new ModificationO(
                        originEntry.getOutboundEntryID(), record.toString()));
            }
            else {
                logger.warn("nothing changed, begin rolling back");
                throw new RuntimeException();
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }

    }

    @Transactional
    @Override
    public void deleteEntry(String id) {
        try {
            outboundEntryMapper.deleteProductsByEntryID(id);
            outboundEntryMapper.deleteEntryByID(id);
        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Deletion failed");
            throw e;
        }

        //todo: add stock
    }

    @Transactional
    @Override
    public void returnEntry(OutboundEntryWithProductsVO returnVO) {

        OutboundEntryDO modifiedEntry = new OutboundEntryDO();
        BeanUtils.copyProperties(returnVO, modifiedEntry);
        List<OutboundProductO> modifiedProducts = returnVO.getOutboundProducts();

        try {
            String id = modifiedEntry.getOutboundEntryID();
            OutboundEntryDO originEntry = outboundEntryMapper.selectEntryForCompare(id);

            StringBuilder record = new StringBuilder("退货记录: 修改者: " + modifiedEntry.getDrawer() + "; ");
            boolean bool = false;
            if (!originEntry.getRemark().equals(modifiedEntry.getRemark())) {
                bool = true;
                record.append(String.format("备注: %s -> %s; ", originEntry.getRemark(),
                        modifiedEntry.getRemark()));
            }
            if (new BigDecimal(originEntry.getTotalAmount())
                    .compareTo(new BigDecimal(modifiedEntry.getTotalAmount())) != 0) {
                bool = true;
                record.append(String.format("总金额: %s -> %s; ", originEntry.getTotalAmount(),
                        modifiedEntry.getTotalAmount()));
            }
            if (bool) {
                outboundEntryMapper.updateEntry(modifiedEntry);
            }

            boolean bool2 = false;
            List<OutboundProductO> originProducts = outboundEntryMapper.selectProductsForCompare(id);
            for (var modifiedProduct : modifiedProducts) {
                String modelCode = modifiedProduct.getCode();
                //compare product
                for (var originProduct : originProducts) {
                    if (originProduct.getOutboundProductID() == modifiedProduct.getOutboundProductID()) {
                        if (!modifiedProduct.getQuantity().equals(originProduct.getQuantity())) {
                            bool2 = true;
                            record.append(String.format("型号(%s) 数量: %d -> %d; ", modelCode,
                                    originProduct.getQuantity(), modifiedProduct.getQuantity()));
                            outboundEntryMapper.returnProductByID(modifiedProduct);
                        }
                        break;
                    }
                }
            }

            if (bool || bool2) {
                modificationMapper.insertModificationRecord(new ModificationO(id, record.toString()));
            }
            else {
                logger.warn("nothing changed begin rolling back");
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
    public List<OutboundProductO> getNotCheckedOutProducts(int companyID, String invoiceType) {
        try {
            List<String> entryIDs = outboundEntryMapper.queryEntriesByCompanyIDAndInvoiceType(companyID, invoiceType);

            List<OutboundProductO> products = new ArrayList<>();
            for (var entryID : entryIDs) {
                List<OutboundProductO> tempProducts = outboundEntryMapper.queryProductsByEntryID(entryID);
                for (var tempProduct : tempProducts) {
                    if (tempProduct.getCheckoutSerial().equals("")) {
                        products.add(tempProduct);
                    }
                }
            }
            return products;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<OutboundProductO> getCheckoutButNotInvoicedProducts(int companyID, String invoiceType) {
        try {
            List<String> entryIDs = outboundEntryMapper.queryEntriesByCompanyIDAndInvoiceType(companyID, invoiceType);

            List<OutboundProductO> products = new ArrayList<>();
            for (var entryID : entryIDs) {
                List<OutboundProductO> tempProducts = outboundEntryMapper.queryProductsByEntryID(entryID);
                for (var tempProduct : tempProducts) {
                    if (!tempProduct.getCheckoutSerial().equals("") && tempProduct.getInvoiceSerial().equals("")) {
                        products.add(tempProduct);
                    }
                }
            }
            return products;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateProductsWithCheckoutSerial(List<OutboundProductO> products, String checkoutSerial) {
        try {
            for (var product : products) {
                product.setCheckoutSerial(checkoutSerial);
                outboundEntryMapper.updateProductsWithCheckoutSerial(product);
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateProductsWithInvoiceSerial(List<OutboundProductO> products, String invoiceSerial) {
        try {
            for (var product : products) {
                product.setInvoiceSerial(invoiceSerial);
                outboundEntryMapper.updateProductsWithInvoiceSerial(product);
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<OutboundProductO> getProductsWithCheckoutSerial(String checkoutSerial){
        try {
            return outboundEntryMapper.getProductsWithCheckoutSerial(checkoutSerial);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<OutboundProductO> getProductsWithInvoiceSerial(String invoiceSerial){
        try {
            return outboundEntryMapper.getProductsWithInvoiceSerial(invoiceSerial);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateEntryWithShippingCostSerial(OutboundEntryDO outboundEntryDO) {
        try {
            outboundEntryMapper.updateEntryWithShippingCostSerial(outboundEntryDO);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<OutboundEntryDO> getEntriesWithShippingCostSerial(String shippingCostSerial) {
        try {
            return outboundEntryMapper.getEntriesWithShippingCostSerial(shippingCostSerial);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<OutboundEntryWithProductsVO> getEntriesByCompanyAndShippingCostType(
            int companyID, String shippingCostType) {
        try {
            List<OutboundEntryDO> entriesFromDatabase = outboundEntryMapper.getEntriesByCompanyAndShippingCostType(
                    companyID, shippingCostType);

            List<OutboundEntryWithProductsVO> entries = new ArrayList<>();
            for (var entryFromDatabase : entriesFromDatabase) {
                //filter out all entries which has already checked-out shipping cost
                if (entryFromDatabase.getShippingCostSerial().equals("")) {
                    OutboundEntryWithProductsVO entry = new OutboundEntryWithProductsVO();
                    BeanUtils.copyProperties(entryFromDatabase, entry);
                    entries.add(entry);
                }
            }
            return entries;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<InvoiceStatVO> getNotYetCheckoutSummary() {
        try {
            List<InvoiceStatDO> statsFromDatabase = outboundEntryMapper.queryNotYetCheckoutSummary();

            return MyUtils.summingUpTotalAmountForEachCompany(statsFromDatabase);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<OutboundProductO> getNotYetCheckoutDetailByCompanyID(int companyID) {
        try {
            return outboundEntryMapper.queryNotYetCheckoutDetailByCompanyID(companyID);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<InvoiceStatVO> getNotYetInvoiceSummary() {
        try {
            List<InvoiceStatDO> statsFromDatabase = outboundEntryMapper.queryNotYetInvoiceSummary();

            return MyUtils.summingUpTotalAmountForEachCompany(statsFromDatabase);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<OutboundProductO> getNotYetInvoiceDetailByCompanyID(int companyID) {
        try {
            return outboundEntryMapper.queryNotYetInvoiceDetailByCompanyID(companyID);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<OutboundProductO> getProductsByWarehouseID(int id) {
        try {
            return outboundEntryMapper.queryProductsByWarehouseStockID(id);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }
}
