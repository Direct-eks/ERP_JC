package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.CheckoutEntryMapper;
import org.jc.backend.dao.InboundEntryMapper;
import org.jc.backend.dao.ModificationMapper;
import org.jc.backend.entity.DO.CheckoutEntryDO;
import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.ModificationO;
import org.jc.backend.entity.VO.CheckoutEntryWithProductsVO;
import org.jc.backend.service.CheckoutEntryService;
import org.jc.backend.service.InboundEntryService;
import org.jc.backend.service.InvoiceEntryService;
import org.jc.backend.service.MoneyEntryService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CheckoutEntryServiceImpl implements CheckoutEntryService {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutEntryServiceImpl.class);

    private final CheckoutEntryMapper checkoutEntryMapper;
    private final MoneyEntryService moneyEntryService;
    private final InboundEntryService inboundEntryService;
    private final InvoiceEntryService invoiceEntryService;
    private final ModificationMapper modificationMapper;

    public CheckoutEntryServiceImpl(CheckoutEntryMapper checkoutEntryMapper,
                                    MoneyEntryService moneyEntryService,
                                    InboundEntryService inboundEntryService,
                                    InvoiceEntryService invoiceEntryService,
                                    ModificationMapper modificationMapper) {
        this.checkoutEntryMapper = checkoutEntryMapper;
        this.moneyEntryService = moneyEntryService;
        this.inboundEntryService = inboundEntryService;
        this.invoiceEntryService = invoiceEntryService;
        this.modificationMapper = modificationMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    public void createEntry(CheckoutEntryWithProductsVO checkoutEntryWithProductsVO,
                            boolean isInbound, boolean isReturn) {

        try {
            CheckoutEntryDO checkoutEntry = new CheckoutEntryDO();
            BeanUtils.copyProperties(checkoutEntryWithProductsVO, checkoutEntry);

            String prefix = isInbound ? (isReturn ? "出退" : "入结") : (isReturn ? "入退" : "出结");
            int count = checkoutEntryMapper.countNumberOfEntriesOfToday(prefix);
            String newCheckoutSerial = MyUtils.formNewSerial(prefix, count);

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
                        checkoutEntryWithProductsVO.getCheckoutProducts(), newCheckoutSerial);
            }
            else {
                //todo outboundEntryService
            }

            //check if it is needed to create invoiceEntry
            if (checkoutEntryWithProductsVO.getInvoiceEntry() != null) {
                String newInvoiceSerial = invoiceEntryService.createEntryForCheckout(
                        checkoutEntryWithProductsVO, true);

                //update product invoiceSerial
                if (isInbound) {
                    inboundEntryService.updateProductsWithInvoiceSerial(
                            checkoutEntryWithProductsVO.getCheckoutProducts(), newInvoiceSerial);
                }
                else {
                    // todo outboundEntryService
                }
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production
            logger.error("insert failed");
            throw e;
        }

    }

    @Transactional(readOnly = true)
    public List<CheckoutEntryWithProductsVO> getEntriesInDateRange(boolean isInbound, Date startDate, Date endDate,
                                                                   int companyID, String invoiceType) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<CheckoutEntryWithProductsVO> entries = new ArrayList<>();
        try {
            String prefix1 = isInbound ? "出退" : "入退";
            String prefix2 = isInbound ? "入结" : "出结";

            List<CheckoutEntryDO> entriesFromDatabase = checkoutEntryMapper.getEntriesInDateRangeByInvoiceTypeAndCompanyID(
                    dateFormat.format(startDate), dateFormat.format(endDate), companyID, invoiceType,
                    prefix1, prefix2);

            for (var entryFromDatabase : entriesFromDatabase) {
                CheckoutEntryWithProductsVO tempEntry = new CheckoutEntryWithProductsVO();
                BeanUtils.copyProperties(entryFromDatabase, tempEntry);

                List<InboundProductO> products = inboundEntryService.getProductsWithCheckoutSerial(
                        tempEntry.getCheckoutEntrySerial());
                tempEntry.setCheckoutProducts(products);

                entries.add(tempEntry);
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); //todo remove in production
            logger.error("query failed");
            throw e;
        }

        return entries;
    }

    @Transactional
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
            }
            else {
                logger.warn("nothing modified");
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production
            logger.error("update failed");
            throw e;
        }

    }

    private boolean compareEntryAndFormModificationRecord(StringBuilder record, CheckoutEntryDO modifiedDO,
                                            CheckoutEntryDO originDO) {
        boolean bool = false;
        if (!modifiedDO.getPaymentMethod().equals(originDO.getPaymentMethod())) {
            bool = true;
            record.append(String.format("付款方式: %s -> %s; ", originDO.getPaymentMethod(), modifiedDO.getPaymentMethod()));
        }
        if (!modifiedDO.getPaymentNumber().equals(originDO.getPaymentNumber())) {
            bool = true;
            record.append(String.format("付款号码: %s -> %s; ", originDO.getPaymentNumber(), modifiedDO.getPaymentNumber()));
        }
        if (modifiedDO.getPaymentAmount() != originDO.getPaymentAmount()) {
            bool = true;
            record.append(String.format("付款金额: %f -> %f; ", originDO.getPaymentAmount(), modifiedDO.getPaymentAmount()));
        }
        if (modifiedDO.getBankAccountID() != originDO.getBankAccountID()) {
            bool = true;
            record.append(String.format("银行账号: %s -> %s; ", originDO.getBankAccountName(), modifiedDO.getBankAccountName()));
        }
        if (modifiedDO.getIsRounded() != originDO.getIsRounded()) {
            bool = true;
            record.append(String.format("抹零状态: %s -> %s; ", originDO.getIsRounded() == 0 ? "不抹零" : "抹零",
                    modifiedDO.getIsRounded() == 0 ? "不抹零" : "抹零"));
        }
        if (modifiedDO.getRoundedAmount() != originDO.getRoundedAmount()) {
            bool = true;
            record.append(String.format("抹零金额: %f -> %f; ", originDO.getRoundedAmount(), modifiedDO.getRoundedAmount()));
        }
        if (modifiedDO.getDebt() != originDO.getDebt()) {
            bool = true;
            record.append(String.format("余额: %f -> %f; ", originDO.getDebt(), modifiedDO.getDebt()));
        }
        if (modifiedDO.getServiceFee() != originDO.getServiceFee()) {
            bool = true;
            record.append(String.format("服务费: %f -> %f;", originDO.getServiceFee(), modifiedDO.getServiceFee()));
        }
        if (!modifiedDO.getRemark().equals(originDO.getRemark())) {
            bool = true;
            record.append(String.format("备注: %s -> %s; ", originDO.getRemark(), modifiedDO.getRemark()));
        }

        return bool;
    }
}
