package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.dao.CheckoutEntryMapper;
import org.jc.backend.dao.ModificationMapper;
import org.jc.backend.entity.DO.CheckoutEntryDO;
import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.ModelCategoryO;
import org.jc.backend.entity.ModificationO;
import org.jc.backend.entity.OutboundProductO;
import org.jc.backend.entity.StatO.CheckoutSummaryO;
import org.jc.backend.entity.StatO.OutboundSpecialSummaryO;
import org.jc.backend.entity.VO.CheckoutEntryWithProductsVO;
import org.jc.backend.entity.VO.InboundEntryWithProductsVO;
import org.jc.backend.entity.VO.OutboundEntryWithProductsVO;
import org.jc.backend.service.*;
import org.jc.backend.utils.CompanyClassification;
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
import java.util.stream.Collectors;

import static org.jc.backend.utils.EntryClassification.*;

@Service
public class CheckoutEntryServiceImpl implements CheckoutEntryService {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutEntryServiceImpl.class);

    private final CheckoutEntryMapper checkoutEntryMapper;
    private final InboundEntryService inboundEntryService;
    private final OutboundEntryService outboundEntryService;
    private final MoneyEntryService moneyEntryService;
    private final InvoiceEntryService invoiceEntryService;
    private final ModificationMapper modificationMapper;
    private final MiscellaneousDataService miscellaneousDataService;
    private final ModelService modelService;
    private final FactoryBrandService factoryBrandService;
    private final CompanyService companyService;

    public CheckoutEntryServiceImpl(CheckoutEntryMapper checkoutEntryMapper,
                                    InboundEntryService inboundEntryService,
                                    OutboundEntryService outboundEntryService,
                                    MoneyEntryService moneyEntryService,
                                    InvoiceEntryService invoiceEntryService,
                                    ModificationMapper modificationMapper,
                                    MiscellaneousDataService miscellaneousDataService,
                                    ModelService modelService,
                                    FactoryBrandService factoryBrandService,
                                    CompanyService companyService) {
        this.checkoutEntryMapper = checkoutEntryMapper;
        this.inboundEntryService = inboundEntryService;
        this.outboundEntryService = outboundEntryService;
        this.moneyEntryService = moneyEntryService;
        this.invoiceEntryService = invoiceEntryService;
        this.modificationMapper = modificationMapper;
        this.miscellaneousDataService = miscellaneousDataService;
        this.modelService = modelService;
        this.factoryBrandService = factoryBrandService;
        this.companyService = companyService;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    @Override
    public void createEntry(CheckoutEntryWithProductsVO checkoutEntryWithProductsVO,
                            boolean isInbound, boolean isReturn) throws GlobalParamException {

        CheckoutEntryDO checkoutEntry = new CheckoutEntryDO();
        BeanUtils.copyProperties(checkoutEntryWithProductsVO, checkoutEntry);

        try {
            Subject subject = SecurityUtils.getSubject();
            String username = (String) subject.getPrincipals().getPrimaryPrincipal();
            String permittedAmount = miscellaneousDataService.getPermittedRoundingAmountByUser(username);

            if (new BigDecimal(permittedAmount).compareTo(new BigDecimal(checkoutEntry.getRoundedAmount())) < 0) {
                throw new GlobalParamException("抹零值超出限定");
            }

            String prefix = isInbound ? INBOUND_CHECKOUT : OUTBOUND_CHECKOUT;
            int count = checkoutEntryMapper.countNumberOfEntriesOfToday(checkoutEntry.getCheckoutDate(), prefix);
            String newCheckoutSerial = MyUtils.formNewSerial(prefix, count, checkoutEntry.getCheckoutDate());

            checkoutEntry.setCheckoutEntrySerial(newCheckoutSerial);

            //first create a new moneyEntry
            String newMoneySerial = moneyEntryService.createEntryForCheckout(
                    checkoutEntry,newCheckoutSerial, isInbound);

            //then create a new checkoutEntry
            checkoutEntry.setMoneyEntrySerial(newMoneySerial);
            checkoutEntryMapper.insertEntry(checkoutEntry);

            //update product checkoutSerial
            if (isInbound) {
                inboundEntryService.updateProductsWithCheckoutSerial(
                        checkoutEntryWithProductsVO.getInboundCheckoutProducts(), newCheckoutSerial);
            }
            else {
                outboundEntryService.updateProductsWithCheckoutSerial(
                        checkoutEntryWithProductsVO.getOutboundCheckoutProducts(), newCheckoutSerial);
            }

            //check if it is needed to create invoiceEntry
            if (checkoutEntryWithProductsVO.getInvoiceEntry() != null) {
                // fill in checkout entry serial
                checkoutEntryWithProductsVO.setCheckoutEntrySerial(newCheckoutSerial);
                String newInvoiceSerial = invoiceEntryService.createEntryForCheckout(
                        checkoutEntryWithProductsVO, true);

                // update checkout entry
                checkoutEntryMapper.updateEntryWithInvoice(newCheckoutSerial, newInvoiceSerial);

                //update product invoiceSerial
                if (isInbound) {
                    inboundEntryService.updateProductsWithInvoiceSerial(
                            checkoutEntryWithProductsVO.getInboundCheckoutProducts(), newInvoiceSerial);
                }
                else {
                    outboundEntryService.updateProductsWithInvoiceSerial(
                            checkoutEntryWithProductsVO.getOutboundCheckoutProducts(), newInvoiceSerial);
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
    public List<CheckoutEntryWithProductsVO> getEntriesInDateRange(boolean isInbound, Date startDate, Date endDate,
                                                                   int companyID, String invoiceType) {
        try {
            String prefix = isInbound ? INBOUND_CHECKOUT : OUTBOUND_CHECKOUT;

            List<CheckoutEntryDO> entriesFromDatabase = checkoutEntryMapper
                    .getEntriesInDateRangeByInvoiceTypeAndCompanyID(
                            MyUtils.dateFormat.format(startDate), MyUtils.dateFormat.format(endDate), companyID,
                            invoiceType, prefix);

            List<CheckoutEntryWithProductsVO> entries = new ArrayList<>();
            for (var entryFromDatabase : entriesFromDatabase) {
                CheckoutEntryWithProductsVO tempEntry = new CheckoutEntryWithProductsVO();
                BeanUtils.copyProperties(entryFromDatabase, tempEntry);

                if (isInbound) {
                    List<InboundProductO> products = inboundEntryService.getProductsWithCheckoutSerial(
                            tempEntry.getCheckoutEntrySerial());
                    tempEntry.setInboundCheckoutProducts(products);
                    tempEntry.setOutboundCheckoutProducts(new ArrayList<>());
                }
                else {
                    List<OutboundProductO> products = outboundEntryService.getProductsWithCheckoutSerial(
                            tempEntry.getCheckoutEntrySerial());
                    tempEntry.setOutboundCheckoutProducts(products);
                    tempEntry.setInboundCheckoutProducts(new ArrayList<>());
                }

                // query related invoice
                tempEntry.setInvoiceEntry(invoiceEntryService.getInvoiceEntryByCheckoutSerial(
                        entryFromDatabase.getCheckoutEntrySerial()));

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
    public void modifyEntry(CheckoutEntryWithProductsVO modifyVO) {
        try {
            CheckoutEntryDO modifyDO = new CheckoutEntryDO();
            BeanUtils.copyProperties(modifyVO, modifyDO);

            CheckoutEntryDO originDO = checkoutEntryMapper.selectEntryBySerialForCompare(
                            modifyDO.getCheckoutEntrySerial());

            StringBuilder record = new StringBuilder("修改者: " + modifyDO.getDrawer() + "; ");
            boolean bool = compareEntryAndFormModificationRecord(record, modifyDO, originDO);

            if (bool) {
                checkoutEntryMapper.modifyEntry(modifyDO);

                modificationMapper.insertModificationRecord(new ModificationO(
                        originDO.getCheckoutEntrySerial(), record.toString()));

                // check and update corresponding moneyEntry
                moneyEntryService.modifyEntryForCheckout(modifyDO);
            }
            else {
                logger.warn("nothing modified, begin rolling back");
                throw new RuntimeException();
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    private static boolean compareEntryAndFormModificationRecord(StringBuilder record, CheckoutEntryDO modifiedDO,
                                            CheckoutEntryDO originDO) {
        boolean bool = false;
        if (!modifiedDO.getPaymentMethod().equals(originDO.getPaymentMethod())) {
            bool = true;
            record.append(String.format("付款方式: %s -> %s; ",
                    originDO.getPaymentMethod(), modifiedDO.getPaymentMethod()));
        }
        if (!modifiedDO.getPaymentNumber().equals(originDO.getPaymentNumber())) {
            bool = true;
            record.append(String.format("付款号码: %s -> %s; ",
                    originDO.getPaymentNumber(), modifiedDO.getPaymentNumber()));
        }
        if (new BigDecimal(modifiedDO.getPaymentAmount()).compareTo(new BigDecimal(originDO.getPaymentAmount())) != 0) {
            bool = true;
            record.append(String.format("付款金额: %s -> %s; ",
                    originDO.getPaymentAmount(), modifiedDO.getPaymentAmount()));
        }
        if (modifiedDO.getBankAccountID() != originDO.getBankAccountID()) {
            bool = true;
            record.append(String.format("银行账号: %s -> %s; ",
                    originDO.getBankAccountName(), modifiedDO.getBankAccountName()));
        }
        if (modifiedDO.getIsRounded() != originDO.getIsRounded()) {
            bool = true;
            record.append(String.format("抹零状态: %s -> %s; ", originDO.getIsRounded() == 0 ? "不抹零" : "抹零",
                    modifiedDO.getIsRounded() == 0 ? "不抹零" : "抹零"));
        }
        if (new BigDecimal(modifiedDO.getRoundedAmount()).compareTo(new BigDecimal(originDO.getRoundedAmount())) != 0) {
            bool = true;
            record.append(String.format("抹零金额: %s -> %s; ",
                    originDO.getRoundedAmount(), modifiedDO.getRoundedAmount()));
        }
        if (new BigDecimal(modifiedDO.getDebt()).compareTo(new BigDecimal(originDO.getDebt())) != 0) {
            bool = true;
            record.append(String.format("余额: %s -> %s; ", originDO.getDebt(), modifiedDO.getDebt()));
        }
        if (new BigDecimal(modifiedDO.getServiceFee()).compareTo(new BigDecimal(originDO.getServiceFee())) != 0) {
            bool = true;
            record.append(String.format("服务费: %s -> %s;", originDO.getServiceFee(), modifiedDO.getServiceFee()));
        }
        if (!modifiedDO.getRemark().equals(originDO.getRemark())) {
            bool = true;
            record.append(String.format("备注: %s -> %s; ", originDO.getRemark(), modifiedDO.getRemark()));
        }

        return bool;
    }

    @Transactional
    @Override
    public void returnEntry(CheckoutEntryWithProductsVO returnVO,
                            boolean isInbound) throws GlobalParamException  {
        try {
            String returnSerial;
            if (isInbound) { // create new outbound entry for inbound return
                OutboundEntryWithProductsVO outVO = new OutboundEntryWithProductsVO();
                outVO.setShipmentDate(MyUtils.todayDateString());
                outVO.setCreationDate(MyUtils.todayDateString());
                outVO.setTotalAmount(returnVO.getTotalAmount()); // todo, amount?
                outVO.setInvoiceType(returnVO.getInvoiceType());
                outVO.setTaxRate(-1); // todo
                outVO.setDrawer(returnVO.getDrawer());
                outVO.setPartnerCompanyID(returnVO.getPartnerCompanyID());
                outVO.setDepartmentID(returnVO.getDepartmentID());
                outVO.setWarehouseID(-1); // todo
                outVO.setRemark(outVO.getRemark());
                outVO.setClassification(INBOUND_RETURN);
                outVO.setShippingCost("0");
                outVO.setShippingCostType("无");
                outVO.setShippingQuantity(0);
                outVO.setShippingNumber("");
                outVO.setShippingMethodID(-1);

                // extract and transform products
                List<OutboundProductO> returnProducts = new ArrayList<>();
                for (var p :  returnVO.getInboundCheckoutProducts()) {
                    var np = new OutboundProductO();
                    BeanUtils.copyProperties(p, np);
                    returnProducts.add(np);
                }
                outVO.setTaxRate(returnProducts.get(0).getTaxRate());
                outVO.setWarehouseID(returnProducts.get(0).getWarehouseID());

                outVO.setOutboundProducts(returnProducts);

                returnSerial = outboundEntryService.createEntry(outVO);
            }
            else {
                InboundEntryWithProductsVO inVO = new InboundEntryWithProductsVO();
                inVO.setEntryDate(MyUtils.todayDateString());
                inVO.setCreationDate(MyUtils.todayDateString());
                inVO.setTotalCost(returnVO.getTotalAmount()); // todo, amount?
                inVO.setInvoiceType(returnVO.getInvoiceType());
                inVO.setDrawer(returnVO.getDrawer());
                inVO.setPartnerCompanyID(returnVO.getPartnerCompanyID());
                inVO.setDepartmentID(returnVO.getDepartmentID());
                inVO.setRemark(returnVO.getRemark());
                inVO.setClassification(OUTBOUND_RETURN);
                inVO.setShippingCost("0");
                inVO.setShippingCostType("无");
                inVO.setShippingQuantity(0);
                inVO.setShippingNumber("");
                inVO.setShippingMethodID(-1);

                // extract and transform products
                List<InboundProductO> returnProducts = new ArrayList<>();
                for (var p :  returnVO.getOutboundCheckoutProducts()) {
                    var np = new InboundProductO();
                    BeanUtils.copyProperties(p, np);
                    returnProducts.add(np);
                }
                inVO.setTaxRate(returnProducts.get(0).getTaxRate());
                inVO.setWarehouseID(returnProducts.get(0).getWarehouseID());

                inVO.setInboundProducts(returnProducts);

                try {
                    returnSerial = inboundEntryService.createEntry(inVO);
                } catch (GlobalParamException e) {
                    String s = "The new generated inbound entry cannot be " +
                            "inserted because there exists presale on the" +
                            "returned products.";
                    logger.info(s);
                    throw new GlobalParamException(s);
                }
            }

            // update return serial for current checkout serial
            returnVO.setReturnSerial(returnSerial);
            checkoutEntryMapper.returnEntry(returnVO);

            // create new checkout entry for newly created inbound/outbound entry
            if (isInbound) { // refill outbound products for inbound return
                returnVO.setOutboundCheckoutProducts(outboundEntryService.getProductsWithEntryID(returnSerial));
            }
            else {
                returnVO.setInboundCheckoutProducts(inboundEntryService.getProductsWithEntryID(returnSerial));
            }
            this.createEntry(returnVO, !isInbound, true); // inverse isInbound


        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<CheckoutSummaryO> getCheckoutSummary(boolean accurate, boolean isInbound, int companyID, String startDate, String endDate,
                                                     int categoryID, String factoryBrand, int warehouseID, int departmentID) {
        try {
            String treeLevel = modelService.getTreeLevelByCategoryID(categoryID);
            factoryBrand = factoryBrand.isBlank() ? "" : factoryBrand;


            List<CheckoutSummaryO> list;
            if (isInbound) {
                list = checkoutEntryMapper.getInboundSummary(startDate, endDate, companyID, treeLevel,
                        factoryBrand, warehouseID, departmentID);
            }
            else {
                list = checkoutEntryMapper.getOutboundSummary(startDate, endDate, companyID, treeLevel,
                        factoryBrand, warehouseID, departmentID);
            }
            if (accurate) {
                list.forEach(item -> {
                    BigDecimal unitPriceWithTax = new BigDecimal(item.getUnitPriceWithTax());
                    item.setTotalPrice(unitPriceWithTax.multiply(BigDecimal.valueOf(item.getQuantity())).toPlainString());
                });
            }
            else {
                list.forEach(item -> {
                    double unitPriceWithTax = Double.parseDouble(item.getUnitPriceWithTax());
                    item.setTotalPrice(Double.toString(unitPriceWithTax * item.getQuantity()));
                });
            }
            return list;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<OutboundSpecialSummaryO> getCheckoutSummaryByParentCategory(String startDate, String endDate) {
        try {
            List<OutboundSpecialSummaryO> results = new ArrayList<>();
            double total = 0;

            for (var c: modelService.getModelCategories()) {
                if (c.getTreeLevel().length() == 1) {
                    var list = checkoutEntryMapper.getOutboundSummary(startDate, endDate,
                            -1, c.getTreeLevel(), "", -1, -1);
                    double totalPrice = 0;
                    for (var item : list) {
                        totalPrice += Double.parseDouble(item.getUnitPriceWithoutTax()) * item.getQuantity();
                    }
                    OutboundSpecialSummaryO summaryO = new OutboundSpecialSummaryO();
                    summaryO.setCategoryCode(c.getCode());
                    summaryO.setCategoryName(c.getName());
                    summaryO.setTotalPrice(String.format("%.2f", totalPrice));
                    results.add(summaryO);
                    total += totalPrice;
                }
            }

            for (var item : results) {
                double percent = total != 0 ?
                        Double.parseDouble(item.getTotalPrice()) / total * 100 : 0;
                item.setPercentage(String.format("%.4f%%", percent));
            }
            return results;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<OutboundSpecialSummaryO> getCheckoutSummaryBySubCategory(String startDate, String endDate, int id) {
        try {
            List<OutboundSpecialSummaryO> results = new ArrayList<>();
            double total = 0;

            var categories = modelService.getModelCategories();
            // extract treeLevel of the given category
            String treeLevel = null;
            for (var c : categories) {
                if (c.getModelCategoryID() == id) {
                    treeLevel = c.getTreeLevel();
                    break;
                }
            }
            // extract categories one level lower
            assert treeLevel != null;
            int treeLevelLength = treeLevel.length() + 2;
            List<ModelCategoryO> subCategories = new ArrayList<>();
            for (var c : categories) {
                if (c.getTreeLevel().startsWith(treeLevel) && c.getTreeLevel().length() == treeLevelLength) {
                    subCategories.add(c);
                }
            }

            for (var c : subCategories) {
                var list = checkoutEntryMapper.getOutboundSummary(startDate, endDate,
                        -1, "", c.getTreeLevel(), -1, -1);
                double totalPrice = 0;
                for (var item : list) {
                    totalPrice += Double.parseDouble(item.getUnitPriceWithoutTax()) * item.getQuantity();
                }
                OutboundSpecialSummaryO summaryO = new OutboundSpecialSummaryO();
                summaryO.setCategoryCode(c.getCode());
                summaryO.setCategoryName(c.getName());
                summaryO.setTotalPrice(String.format("%.2f", totalPrice));
                results.add(summaryO);
                total += totalPrice;
            }

            for (var item : results) {
                double percent = Double.parseDouble(item.getTotalPrice()) / total;
                item.setPercentage(String.format("%.4f%%", percent));
            }
            return results;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<OutboundSpecialSummaryO> getCheckoutSummaryByBrand(String startDate, String endDate) {
        try {
            List<OutboundSpecialSummaryO> results = new ArrayList<>();
            double total = 0;

            for (var brand : factoryBrandService.getAllFactoryBrands()) {
                var list = checkoutEntryMapper.getOutboundSummary(startDate, endDate,
                        -1, "", brand.getCode(), -1, -1);
                double totalPrice = 0;
                for (var item : list) {
                    totalPrice += Double.parseDouble(item.getUnitPriceWithoutTax()) * item.getQuantity();
                }
                OutboundSpecialSummaryO summaryO = new OutboundSpecialSummaryO();
                summaryO.setFactoryBrand(brand.getCode());
                summaryO.setTotalPrice(String.format("%.2f", totalPrice));
                results.add(summaryO);
                total += totalPrice;
            }

            for (var item : results) {
                double percent = total != 0 ?
                        Double.parseDouble(item.getTotalPrice()) / total * 100 : 0;
                item.setPercentage(String.format("%.4f%%", percent));
            }
            return results;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<OutboundSpecialSummaryO> getCheckoutSummaryByCompany(String startDate, String endDate) {
        try {
            List<OutboundSpecialSummaryO> results = new ArrayList<>();
            double total = 0;


            var map = checkoutEntryMapper.getOutboundSummary(startDate, endDate,
                    -1, "", "", -1, -1)
                    .stream().collect(Collectors.groupingBy(CheckoutSummaryO::getCompanyAbbreviatedName));

            for (var entry : map.entrySet()) {
                double totalPrice = 0;
                for (var item : entry.getValue()) {
                    totalPrice += Double.parseDouble(item.getUnitPriceWithoutTax()) * item.getQuantity();
                }
                OutboundSpecialSummaryO summaryO = new OutboundSpecialSummaryO();
                summaryO.setAbbreviatedName(entry.getKey());
                summaryO.setTotalPrice(String.format("%.2f", totalPrice));
                results.add(summaryO);
                total += totalPrice;
            }

            for (var item : results) {
                double percent = total != 0 ?
                        Double.parseDouble(item.getTotalPrice()) / total * 100 : 0;
                item.setPercentage(String.format("%.4f%%", percent));
            }
            return results;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<CheckoutSummaryO> getReceivableSummary(int companyID) throws GlobalParamException {
        try {
            var company = companyService.getCompanyByID(companyID);
            assert company != null;

            switch (company.getClassification()) {
                case CompanyClassification.CUSTOMER:
                case CompanyClassification.OTHER_RECV:
                    break;
                default:
                    throw new GlobalParamException("此公司为供应商/其他应付");
            }

            return null; // todo

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<CheckoutSummaryO> getPayableSummary(int companyID) throws GlobalParamException {
        try {
            var company = companyService.getCompanyByID(companyID);
            assert company != null;

            switch (company.getClassification()) {
                case CompanyClassification.SUPPLIER:
                case CompanyClassification.OTHER_PAY:
                    break;
                default:
                    throw new GlobalParamException("此公司为客户/其他应收");
            }

            return null; // todo

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void auditEntriesByMonth(boolean isInbound, String month) throws GlobalParamException {
        try {
            var pair = MyUtils.getFirstAndLastDayOfMonth(month);

            List<CheckoutSummaryO> list;
            if (isInbound) {
                list = checkoutEntryMapper.getInboundSummary(pair.getLeft(), pair.getRight(),
                        -1, "", "", -1, -1);
            }
            else {
                list = checkoutEntryMapper.getOutboundSummary(pair.getLeft(), pair.getRight(),
                        -1, "", "", -1, -1);
            }
            for (var s : list) {
                checkoutEntryMapper.updateVerifiedEntry(s.getCheckoutEntrySerial(), 1);
            }

            miscellaneousDataService.addNewAuditMonth(month, isInbound ? "入库" : "出库");

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Update failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void deleteAuditMonth(String month, String value) throws GlobalParamException {
        try {
            var pair = MyUtils.getFirstAndLastDayOfMonth(month);

            List<CheckoutSummaryO> list;
            list = checkoutEntryMapper.getInboundSummary(pair.getLeft(), pair.getRight(),
                    -1, "", "", -1, -1);
            for (var s : list) {
                checkoutEntryMapper.updateVerifiedEntry(s.getCheckoutEntrySerial(), 0);
            }
            list = checkoutEntryMapper.getOutboundSummary(pair.getLeft(), pair.getRight(),
                    -1, "", "", -1, -1);
            for (var s : list) {
                checkoutEntryMapper.updateVerifiedEntry(s.getCheckoutEntrySerial(), 0);
            }

            miscellaneousDataService.deleteAuditMonth(month, value);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Update failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<CheckoutSummaryO> getDiffStat(int companyID, String startDate, String endDate,
                                              int categoryID, String factoryBrand, int departmentID) {
        try {
            var list = checkoutEntryMapper.getOutboundSummary(startDate, endDate, companyID,
                    "", factoryBrand, -1, departmentID);

            list.forEach(item -> {
                double unitPriceWithTax = Double.parseDouble(item.getUnitPriceWithTax());
                double price = unitPriceWithTax * item.getQuantity();
                item.setTotalPrice(Double.toString(price));
                double stockUnitPrice = Double.parseDouble(item.getStockUnitPrice());
                double cost = stockUnitPrice * item.getQuantity();
                item.setGrossProfit(Double.toString(price - cost));
                item.setGrossProfitRate((price - cost) / cost + "%");
            });
            return list;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }
}
