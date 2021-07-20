package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.dao.CheckoutEntryMapper;
import org.jc.backend.dao.ModificationMapper;
import org.jc.backend.entity.DO.CheckoutEntryDO;
import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.ModificationO;
import org.jc.backend.entity.OutboundProductO;
import org.jc.backend.entity.VO.CheckoutEntryWithProductsVO;
import org.jc.backend.entity.VO.InboundEntryWithProductsVO;
import org.jc.backend.entity.VO.OutboundEntryWithProductsVO;
import org.jc.backend.service.*;
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
public class CheckoutEntryServiceImpl implements CheckoutEntryService {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutEntryServiceImpl.class);

    private final CheckoutEntryMapper checkoutEntryMapper;
    private final InboundEntryService inboundEntryService;
    private final OutboundEntryService outboundEntryService;
    private final MoneyEntryService moneyEntryService;
    private final InvoiceEntryService invoiceEntryService;
    private final ModificationMapper modificationMapper;
    private final MiscellaneousDataService miscellaneousDataService;

    public CheckoutEntryServiceImpl(CheckoutEntryMapper checkoutEntryMapper,
                                    InboundEntryService inboundEntryService,
                                    OutboundEntryService outboundEntryService,
                                    MoneyEntryService moneyEntryService,
                                    InvoiceEntryService invoiceEntryService,
                                    ModificationMapper modificationMapper,
                                    MiscellaneousDataService miscellaneousDataService) {
        this.checkoutEntryMapper = checkoutEntryMapper;
        this.inboundEntryService = inboundEntryService;
        this.outboundEntryService = outboundEntryService;
        this.moneyEntryService = moneyEntryService;
        this.invoiceEntryService = invoiceEntryService;
        this.modificationMapper = modificationMapper;
        this.miscellaneousDataService = miscellaneousDataService;
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

            String prefix = isInbound ? (isReturn ? "出退" : "入结") : (isReturn ? "入退" : "出结");
            int count = checkoutEntryMapper.countNumberOfEntriesOfToday(prefix);
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
                String newInvoiceSerial = invoiceEntryService.createEntryForCheckout(
                        checkoutEntryWithProductsVO, true);

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
            String prefix1 = isInbound ? "出退" : "入退";
            String prefix2 = isInbound ? "入结" : "出结";

            List<CheckoutEntryDO> entriesFromDatabase = checkoutEntryMapper
                    .getEntriesInDateRangeByInvoiceTypeAndCompanyID(
                            MyUtils.dateFormat.format(startDate), MyUtils.dateFormat.format(endDate), companyID,
                            invoiceType, prefix1, prefix2);

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
                outVO.setClassification("入退");
                outVO.setShippingCost("0");
                outVO.setShippingCostType("无");
                outVO.setShippingQuantity(0);
                outVO.setShippingNumber("");
                outVO.setShippingMethodID(-1);

                List<OutboundProductO> returnProducts = new ArrayList<>();
                for (var p :  returnVO.getInboundCheckoutProducts()) {
                    var np = new OutboundProductO();
                    BeanUtils.copyProperties(p, np);
                    returnProducts.add(np);
                }
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
                inVO.setClassification("出退");
                inVO.setShippingCost("0");
                inVO.setShippingCostType("无");
                inVO.setShippingQuantity(0);
                inVO.setShippingNumber("");
                inVO.setShippingMethodID(-1);

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

            CheckoutEntryDO returnDO = new CheckoutEntryDO();
            BeanUtils.copyProperties(returnVO, returnDO);

            returnDO.setReturnSerial(returnSerial);
            checkoutEntryMapper.returnEntry(returnDO);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }
}
