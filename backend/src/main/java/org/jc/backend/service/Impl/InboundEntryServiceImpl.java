package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.dao.InboundEntryMapper;
import org.jc.backend.dao.ModificationMapper;
import org.jc.backend.entity.DO.InboundEntryDO;
import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.ModificationO;
import org.jc.backend.entity.StatO.InvoiceStatDO;
import org.jc.backend.entity.StatO.InvoiceStatVO;
import org.jc.backend.entity.StatO.ProductStatO;
import org.jc.backend.entity.VO.InboundEntryWithProductsVO;
import org.jc.backend.entity.WarehouseStockO;
import org.jc.backend.service.InboundEntryService;
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
public class InboundEntryServiceImpl implements InboundEntryService {
    private static final Logger logger = LoggerFactory.getLogger(InboundEntryServiceImpl.class);

    private final InboundEntryMapper inboundEntryMapper;
    private final ModificationMapper modificationMapper;
    private final WarehouseStockService warehouseStockService;
    private final OutboundEntryService outboundEntryService;

    public InboundEntryServiceImpl(InboundEntryMapper inboundEntryMapper,
                                   ModificationMapper modificationMapper,
                                   WarehouseStockService warehouseStockService,
                                   OutboundEntryService outboundEntryService) {
        this.inboundEntryMapper = inboundEntryMapper;
        this.modificationMapper = modificationMapper;
        this.warehouseStockService = warehouseStockService;
        this.outboundEntryService = outboundEntryService;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    @Override
    public void createEntry(InboundEntryWithProductsVO entryWithProductsVO) throws GlobalParamException{

        InboundEntryDO newEntry = new InboundEntryDO();
        BeanUtils.copyProperties(entryWithProductsVO, newEntry);
        List<InboundProductO> newProducts = entryWithProductsVO.getInboundProducts();

        // set shipping cost to 0 if it is blank
        if (newEntry.getShippingCost().trim().equals("")) {
            newEntry.setShippingCost("0");
        }

        try {
            // presale checks
            switch (warehouseStockService.passPresaleCheck(newProducts)) {
                case -1:
                    break;
                case 0:
                    // do entryDate check
                    if (!outboundEntryService.passPresaleDateCheck(newProducts, entryWithProductsVO.getEntryDate())) {
                        throw new GlobalParamException("入库单日期必须大于所有预销售产品出库日期");
                    }
                    break;
                case 1:
                    throw new GlobalParamException("入库单不能同时含有预销售和普通入库产品");
            }

            String entryDate = newEntry.getEntryDate();
            int count = inboundEntryMapper.countNumberOfEntriesOfGivenDate(entryDate);
            String newSerial = MyUtils.formNewSerial("购入", count, entryDate);

            // set new inbound entry serial, and insert
            newEntry.setInboundEntryID(newSerial);
            inboundEntryMapper.insertNewEntry(newEntry);

            // set inbound entry serial for products, and insert
            int warehouseID = entryWithProductsVO.getWarehouseID();
            for (var product : newProducts) {
                //set entry serial id for product
                product.setInboundEntryID(newSerial);

                //check warehouseStock for existence, if not, create new one
                int skuID = product.getSkuID();
                if (product.getWarehouseStockID() == -1
                        || warehouseStockService.getWarehouseStockByWarehouseAndSku(warehouseID, skuID) == null) {
                    WarehouseStockO newWarehouseStock = new WarehouseStockO();
                    newWarehouseStock.setSkuID(skuID);
                    newWarehouseStock.setWarehouseID(warehouseID);
                    int newID = warehouseStockService.insertNewWarehouseStock(newWarehouseStock);
                    product.setWarehouseStockID(newID);
                }

                // calculate stock unit price for product, and fill in stock quantity & unit price
                warehouseStockService.increaseStock(product, entryDate);

                // insert
                inboundEntryMapper.insertNewProduct(product);
                int id = product.getInboundProductID();
                logger.info("Insert new inbound product id: {}", id);
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Insert failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<InboundEntryWithProductsVO> getEntriesInDateRangeByTypeAndCompanyID(Date startDate, Date endDate,
                                                                                    String type, int companyID) {
        try {
            List<InboundEntryDO> entriesFromDatabase = inboundEntryMapper.queryEntriesInDateRangeByInvoiceTypeAndCompanyID(
                    MyUtils.dateFormat.format(startDate), MyUtils.dateFormat.format(endDate), type, companyID);

            List<InboundEntryWithProductsVO> entries = new ArrayList<>();
            for (var entryFromDatabase : entriesFromDatabase) {
                InboundEntryWithProductsVO tempEntry = new InboundEntryWithProductsVO();
                BeanUtils.copyProperties(entryFromDatabase, tempEntry);

                List<InboundProductO> products = inboundEntryMapper.queryProductsByEntryID(
                        tempEntry.getInboundEntryID());
                tempEntry.setInboundProducts(products);

                entries.add(tempEntry);
            }
            return entries;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void completeEntry(InboundEntryWithProductsVO inboundEntryWithProductsVO) {

        InboundEntryDO currentInfo = new InboundEntryDO();
        BeanUtils.copyProperties(inboundEntryWithProductsVO, currentInfo);

        // set shipping cost to 0 if it is blank
        if (currentInfo.getShippingCost().trim().equals("")) {
            currentInfo.setShippingCost("0");
        }

        try {
            String id = currentInfo.getInboundEntryID();
            //query database for compare
            InboundEntryDO originInfo = inboundEntryMapper.selectEntryShippingInfoForCompare(id);

            // check changes to shipping info
            StringBuilder record = new StringBuilder("修改者: " + currentInfo.getDrawer() + "; ");
            boolean bool = IOModificationUtils.shippingInfoCompareAndFormModificationRecord(
                    record, currentInfo, originInfo);

            if (bool) {
                inboundEntryMapper.updateShippingInfo(currentInfo);

                logger.info("Completion: " + record);
                modificationMapper.insertModificationRecord(new ModificationO(
                        originInfo.getInboundEntryID(), record.toString()));
            }
            else {
                logger.warn("nothing changed, begin rolling back");
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
    public void modifyEntry(InboundEntryWithProductsVO inboundEntryWithProductsVO) {
        // extract entryDO
        InboundEntryDO currentEntry = new InboundEntryDO();
        BeanUtils.copyProperties(inboundEntryWithProductsVO, currentEntry);

        // extract List<productO>
        List<InboundProductO> currentProducts = inboundEntryWithProductsVO.getInboundProducts();

        try {
            String id = currentEntry.getInboundEntryID();
            logger.info("Serial to be changed: " + id);

            // query database for compare
            InboundEntryDO originalEntry = inboundEntryMapper.selectEntryForCompare(id);
            List<InboundProductO> originalProducts = inboundEntryMapper.selectProductsForCompare(id);

            // compare entry
            StringBuilder record = new StringBuilder("修改者: " + currentEntry.getDrawer() + "; ");
            boolean entryChanged = false;
            boolean productsChanged = false;
            // check if entry info changed
            if (IOModificationUtils.entryCompareAndFormModificationRecord(record, currentEntry, originalEntry)) {
                entryChanged = true;
                inboundEntryMapper.updateEntry(currentEntry);
            }

            for (var originalProduct : originalProducts) {
                boolean found = false;
                for (var currentProduct : currentProducts) {
                    if (currentProduct.getInboundProductID() == originalProduct.getInboundProductID()) {
                        if (IOModificationUtils.productCompareAndFormModificationRecord(
                                record, currentProduct, originalProduct)) {
                            productsChanged = true;
                            inboundEntryMapper.updateProduct(currentProduct);
                            warehouseStockService.modifyStock(currentProduct, currentEntry.getEntryDate());
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    // todo optimize to adapt to delete
                    logger.error("deleted product found");
                    throw new RuntimeException();
                }
            }

            if (entryChanged || productsChanged) {
                logger.info("Modification: " + record);
                modificationMapper.insertModificationRecord(new ModificationO(
                        originalEntry.getInboundEntryID(), record.toString()));
            }
            else {
                logger.warn("nothing changed, begin rolling back");
                throw new RuntimeException();
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Modify failed");
            throw e;
        }

    }

    @Transactional
    @Override
    public void deleteEntry(String id) {
        try {
            inboundEntryMapper.deleteProductsByEntryID(id);
            inboundEntryMapper.deleteEntryByID(id);
        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Deletion failed");
            throw e;
        }

        //todo: deduct stock
    }

    @Transactional
    @Override
    public void returnEntry(InboundEntryWithProductsVO returnVO) {

        InboundEntryDO modifiedEntry = new InboundEntryDO();
        BeanUtils.copyProperties(returnVO, modifiedEntry);
        List<InboundProductO> modifiedProducts = returnVO.getInboundProducts();

        try {
            String id = modifiedEntry.getInboundEntryID();
            InboundEntryDO originEntry = inboundEntryMapper.selectEntryForCompare(id);

            // todo not done
            StringBuilder record = new StringBuilder("退货记录: 修改者: " + modifiedEntry.getDrawer() + "; ");
            boolean bool = false;
            if (!originEntry.getRemark().equals(modifiedEntry.getRemark())) {
                bool = true;
                record.append(String.format("备注: %s -> %s; ", originEntry.getRemark(),
                        modifiedEntry.getRemark()));
            }
            if (new BigDecimal(originEntry.getTotalCost())
                    .compareTo(new BigDecimal(modifiedEntry.getTotalCost())) != 0) {
                bool = true;
                record.append(String.format("总金额: %s -> %s; ", originEntry.getTotalCost(),
                        modifiedEntry.getTotalCost()));
            }
            if (bool) {
                inboundEntryMapper.updateEntry(modifiedEntry);
            }

            boolean bool2 = false;
            List<InboundProductO> originProducts = inboundEntryMapper.selectProductsForCompare(id);
            for (var modifiedProduct : modifiedProducts) {
                String modelCode = modifiedProduct.getCode();
                //compare product
                for (var originProduct : originProducts) {
                    if (originProduct.getInboundProductID() == modifiedProduct.getInboundProductID()) {
                        if (!modifiedProduct.getQuantity().equals(originProduct.getQuantity())) {
                            bool2 = true;
                            record.append(String.format("型号(%s) 数量: %d -> %d; ", modelCode,
                                    originProduct.getQuantity(), modifiedProduct.getQuantity()));
                            inboundEntryMapper.returnProductByID(modifiedProduct);
                        }
                        break;
                    }
                }
            }

            if (bool || bool2) {
                modificationMapper.insertModificationRecord(new ModificationO(id, record.toString()));
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

    @Transactional(readOnly = true)
    @Override
    public List<InboundProductO> getNotCheckedOutProducts(int companyID, String invoiceType) {
        try {
            List<String> entryIDs = inboundEntryMapper.queryEntriesByCompanyIDAndInvoiceType(companyID, invoiceType);

            List<InboundProductO> products = new ArrayList<>();
            for (var entryID : entryIDs) {
                List<InboundProductO> tempProducts = inboundEntryMapper.queryProductsByEntryID(entryID);
                for (var tempProduct : tempProducts) {
                    //filter out checked-out products
                    if (tempProduct.getCheckoutSerial().equals("")) {
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

    @Transactional(readOnly = true)
    @Override
    public List<InboundProductO> getCheckoutButNotInvoicedProducts(int companyID, String invoiceType) {
        try {
            List<String> entryIDs = inboundEntryMapper.queryEntriesByCompanyIDAndInvoiceType(companyID, invoiceType);

            List<InboundProductO> products = new ArrayList<>();
            for (var entryID : entryIDs) {
                List<InboundProductO> tempProducts = inboundEntryMapper.queryProductsByEntryID(entryID);
                for (var tempProduct : tempProducts) {
                    //filter checked-out but not invoiced products
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
    public void updateProductsWithCheckoutSerial(List<InboundProductO> products, String checkoutSerial) {
        try {
            for (var product : products) {
                product.setCheckoutSerial(checkoutSerial);
                inboundEntryMapper.updateProductsWithCheckoutSerial(product);
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Update failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateProductsWithInvoiceSerial(List<InboundProductO> products, String invoiceSerial) {
        try {
            for (var product : products) {
                product.setInvoiceSerial(invoiceSerial);
                inboundEntryMapper.updateProductsWithInvoiceSerial(product);
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<InboundProductO> getProductsWithCheckoutSerial(String checkoutSerial) {
        try {
            return inboundEntryMapper.getProductsWithCheckoutSerial(checkoutSerial);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<InboundProductO> getProductsWithInvoiceSerial(String invoiceSerial) {
        try {
            return inboundEntryMapper.getProductsWithInvoiceSerial(invoiceSerial);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateEntryWithShippingCostSerial(InboundEntryDO inboundEntryDO) {
        try {
            inboundEntryMapper.updateEntryWithShippingCostSerial(inboundEntryDO);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<InboundEntryDO> getEntriesWithShippingCostSerial(String shippingCostSerial) {
        try {
            return inboundEntryMapper.getEntriesWithShippingCostSerial(shippingCostSerial);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<InboundEntryWithProductsVO> getEntriesByCompanyAndShippingCostType(
            int companyID, String shippingCostType) {
        try {
            List<InboundEntryDO> entriesFromDatabase = inboundEntryMapper.getEntriesByCompanyAndShippingCostType(
                    companyID, shippingCostType);

            List<InboundEntryWithProductsVO> entries = new ArrayList<>();
            for (var entryFromDatabase : entriesFromDatabase) {
                //filter out all entries which has already checked-out shipping cost
                if (entryFromDatabase.getShippingCostSerial().equals("")) {
                    InboundEntryWithProductsVO entry = new InboundEntryWithProductsVO();
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
            List<InvoiceStatDO> statsFromDatabase = inboundEntryMapper.queryNotYetCheckoutSummary();

            return MyUtils.summingUpTotalAmountForEachCompany(statsFromDatabase);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<InboundProductO> getNotYetCheckoutDetailByCompanyID(int companyID) {
        try {
            return inboundEntryMapper.queryNotYetCheckoutDetailByCompanyID(companyID);

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
            List<InvoiceStatDO> statsFromDatabase = inboundEntryMapper.queryNotYetInvoiceSummary();

            return MyUtils.summingUpTotalAmountForEachCompany(statsFromDatabase);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<InboundProductO> getNotYetInvoiceDetailByCompanyID(int companyID) {
        try {
            return inboundEntryMapper.queryNotYetInvoiceDetailByCompanyID(companyID);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<InboundProductO> getProductsByWarehouseID(int id) {
        try {
            return inboundEntryMapper.queryProductsByWarehouseStockID(id);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }


    @Transactional(readOnly = true)
    @Override
    public List<ProductStatO> getAllInboundProducts(int id) {
        try {
            var list = inboundEntryMapper.queryAllInboundProductsByWarehouseStockID(id);
            for (var p : list) {
                p.setInbound(true);
            }
            return list;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateInboundProduct(ProductStatO productO) {
        try {
            inboundEntryMapper.updateProductStockInfo(productO);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }
}
