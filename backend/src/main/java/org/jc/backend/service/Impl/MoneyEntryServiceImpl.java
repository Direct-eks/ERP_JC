package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.ModificationMapper;
import org.jc.backend.dao.MoneyEntryMapper;
import org.jc.backend.entity.DO.CheckoutEntryDO;
import org.jc.backend.entity.ModificationO;
import org.jc.backend.entity.MoneyEntryO;
import org.jc.backend.entity.StatO.MoneyEntryDetailO;
import org.jc.backend.service.AccountsStatService;
import org.jc.backend.service.MoneyEntryService;
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

import static org.jc.backend.utils.EntryClassification.INBOUND_PAYABLE;
import static org.jc.backend.utils.EntryClassification.OUTBOUND_RECEIVABLE;

@Service
public class MoneyEntryServiceImpl implements MoneyEntryService, AccountsStatService {
    private static final Logger logger = LoggerFactory.getLogger(MoneyEntryServiceImpl.class);

    private final MoneyEntryMapper moneyEntryMapper;
    private final ModificationMapper modificationMapper;

    public MoneyEntryServiceImpl(MoneyEntryMapper moneyEntryMapper,
                                 ModificationMapper modificationMapper) {
        this.moneyEntryMapper = moneyEntryMapper;
        this.modificationMapper = modificationMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    private static String getPrefix (boolean isInbound) {
        return isInbound ? INBOUND_PAYABLE : OUTBOUND_RECEIVABLE;
    }

    @Transactional
    @Override
    public void createEntry(MoneyEntryO moneyEntryO, boolean isInbound) {
        try {
            String prefix = getPrefix(isInbound);
            int count = moneyEntryMapper.countNumberOfEntriesOfToday(moneyEntryO.getPaymentDate(), prefix);
            String newMoneySerial = MyUtils.formNewSerial(prefix, count, moneyEntryO.getPaymentDate());

            moneyEntryO.setMoneyEntrySerial(newMoneySerial);
            moneyEntryMapper.insertEntry(moneyEntryO);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("insert failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoneyEntryO> getEntriesInDateRange(Date startDate, Date endDate, int companyID,
                                                   String paymentMethod, int bankAccountID, boolean isInbound) {
        try {
            String prefix = getPrefix(isInbound);
            return moneyEntryMapper.getEntriesInDateRangeAndParams(MyUtils.dateFormat.format(startDate),
                    MyUtils.dateFormat.format(endDate), companyID, paymentMethod, bankAccountID, prefix);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoneyEntryO> getEntryBySerial(String serialSuffix, boolean isInbound) {
        try {
            String prefix = getPrefix(isInbound);
            List<MoneyEntryO> result = new ArrayList<>();
            result.add(moneyEntryMapper.selectEntryBySerial(prefix + serialSuffix));
            return result;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void modifyEntry(MoneyEntryO modifiedEntry) {
        try {
            MoneyEntryO originEntry = moneyEntryMapper.selectEntryBySerial(modifiedEntry.getMoneyEntrySerial());

            StringBuilder record = new StringBuilder("修改者: " + modifiedEntry.getDrawer() + "; ");
            boolean bool = compareEntryAndFormModificationRecord(record, originEntry, modifiedEntry);

            if (bool) {
                moneyEntryMapper.modifyEntry(modifiedEntry);

                modificationMapper.insertModificationRecord(new ModificationO(
                        modifiedEntry.getMoneyEntrySerial(), record.toString()));
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

    private static boolean compareEntryAndFormModificationRecord(StringBuilder record, MoneyEntryO originEntry,
                                                          MoneyEntryO modifiedEntry) {
        boolean bool = false;
        if (!originEntry.getPaymentMethod().equals(modifiedEntry.getPaymentMethod())) {
            bool = true;
            record.append(String.format("付款方式: %s -> %s; ", originEntry.getPaymentMethod(),
                    modifiedEntry.getPaymentMethod()));
        }
        if (new BigDecimal(originEntry.getPaymentAmount())
                .compareTo(new BigDecimal(modifiedEntry.getPaymentAmount())) != 0) {
            bool = true;
            record.append(String.format("付款金额: %s -> %s; ", originEntry.getPaymentAmount(),
                    modifiedEntry.getPaymentAmount()));
        }
        if (!originEntry.getPaymentNumber().equals(modifiedEntry.getPaymentNumber())) {
            bool = true;
            record.append(String.format("付款号码: %s -> %s; ", originEntry.getPaymentNumber(),
                    modifiedEntry.getPaymentNumber()));
        }
        if (!originEntry.getBankAccountID().equals(modifiedEntry.getBankAccountID())) {
            bool = true;
            record.append(String.format("银行: %s -> %s", originEntry.getBankAccountName(),
                    modifiedEntry.getBankAccountName()));
        }
        if (!originEntry.getDepartmentID().equals(modifiedEntry.getDepartmentID())) {
            bool = true;
            record.append(String.format("部门: %s -> %s", originEntry.getDepartmentName(),
                    modifiedEntry.getDepartmentName()));
        }
        if (!originEntry.getPaymentIndication().equals(modifiedEntry.getPaymentIndication())) {
            bool = true;
            record.append(String.format("开票类型: %s -> %s; ", originEntry.getPaymentIndication(),
                    modifiedEntry.getPaymentIndication()));
        }
        if (!originEntry.getRemark().equals(modifiedEntry.getRemark())) {
            bool = true;
            record.append(String.format("备注: %s -> %s;", originEntry.getRemark(), modifiedEntry.getRemark()));
        }

        return bool;
    }


    @Transactional
    @Override
    public String createEntryForCheckout(CheckoutEntryDO checkoutEntry, String checkoutSerial, boolean isInbound) {

        MoneyEntryO moneyEntryO = new MoneyEntryO();
        moneyEntryO.setPartnerCompanyID(checkoutEntry.getPartnerCompanyID());
        moneyEntryO.setPaymentIndication("正常");
        moneyEntryO.setPaymentMethod(checkoutEntry.getPaymentMethod());
        moneyEntryO.setPaymentNumber(checkoutEntry.getPaymentNumber());
        moneyEntryO.setPaymentAmount(checkoutEntry.getPaymentAmount());
        moneyEntryO.setBankAccountID(checkoutEntry.getBankAccountID());
        moneyEntryO.setRemark("");
        moneyEntryO.setDrawer(checkoutEntry.getDrawer());
        moneyEntryO.setPaymentDate(checkoutEntry.getCheckoutDate());
        moneyEntryO.setCheckoutSerial(checkoutEntry.getCheckoutEntrySerial());
        moneyEntryO.setDepartmentID(checkoutEntry.getDepartmentID());

        try {
            String prefix = getPrefix(isInbound);
            int count = moneyEntryMapper.countNumberOfEntriesOfToday(moneyEntryO.getPaymentDate(), prefix);
            String newMoneySerial = MyUtils.formNewSerial(prefix, count, moneyEntryO.getPaymentDate());

            moneyEntryO.setCheckoutSerial(checkoutSerial);
            moneyEntryO.setMoneyEntrySerial(newMoneySerial);

            moneyEntryMapper.insertEntry(moneyEntryO);

            return newMoneySerial;

        }
        catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("insert error");
            throw e;
        }
    }

    @Transactional
    @Override
    public void modifyEntryForCheckout(CheckoutEntryDO checkoutEntryDO) {

        MoneyEntryO modifiedEntry = new MoneyEntryO();
        BeanUtils.copyProperties(checkoutEntryDO, modifiedEntry);

        try {
            MoneyEntryO originEntry = moneyEntryMapper.selectEntryBySerial(modifiedEntry.getMoneyEntrySerial());

            StringBuilder record = new StringBuilder("修改者: " + modifiedEntry.getDrawer() + "; ");
            //since only payment fields are needed to be compared, copy fields from originEntry
            // in case of unwanted changes being output to modification record
            modifiedEntry.setPaymentIndication(originEntry.getPaymentIndication());
            modifiedEntry.setRemark(originEntry.getRemark());
            boolean bool = compareEntryAndFormModificationRecord(record, originEntry, modifiedEntry);

            if (bool) {
                moneyEntryMapper.modifyEntry(modifiedEntry);

                modificationMapper.insertModificationRecord(new ModificationO(
                        modifiedEntry.getMoneyEntrySerial(), record.toString()));
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

    /* -------------------------- Accounts Stat Service -------------------------- */

    @Transactional(readOnly = true)
    @Override
    public List<MoneyEntryDetailO> getEntryDetails(boolean isInbound) {
        return null;
    }
}
