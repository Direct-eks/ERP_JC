package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.ShippingCostEntryMapper;
import org.jc.backend.entity.DO.InboundEntryDO;
import org.jc.backend.entity.DO.OutboundEntryDO;
import org.jc.backend.entity.DO.ShippingCostEntryDO;
import org.jc.backend.entity.StatO.AccountsDetailO;
import org.jc.backend.entity.VO.ShippingCostEntryVO;
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

import static org.jc.backend.utils.EntryClassification.SHIPPING_COST_PAY;
import static org.jc.backend.utils.EntryClassification.SHIPPING_COST_RECV;

@Service
public class ShippingCostEntryServiceImpl implements ShippingCostEntryService, AccountsStatService {
    private static final Logger logger = LoggerFactory.getLogger(ShippingCostEntryServiceImpl.class);

    private final ShippingCostEntryMapper shippingCostEntryMapper;
    private final InboundEntryService inboundEntryService;
    private final OutboundEntryService outboundEntryService;
    private final ModificationRecordService modificationRecordService;
    private final AccountsService accountsService;

    public ShippingCostEntryServiceImpl(ShippingCostEntryMapper shippingCostEntryMapper,
                                        InboundEntryService inboundEntryService,
                                        OutboundEntryService outboundEntryService,
                                        ModificationRecordService modificationRecordService,
                                        AccountsService accountsService
    ) {
        this.shippingCostEntryMapper = shippingCostEntryMapper;
        this.inboundEntryService = inboundEntryService;
        this.outboundEntryService = outboundEntryService;
        this.modificationRecordService = modificationRecordService;
        this.accountsService = accountsService;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    private static String getPrefix(boolean isInbound) {
        return isInbound ? SHIPPING_COST_PAY : SHIPPING_COST_RECV;
    }

    @Transactional
    @Override
    public void createEntry(ShippingCostEntryVO shippingCostEntryVO, boolean isInbound) {

        ShippingCostEntryDO shippingCostEntryDO = new ShippingCostEntryDO();
        BeanUtils.copyProperties(shippingCostEntryVO, shippingCostEntryDO);

        List<InboundEntryDO> inboundEntries = shippingCostEntryVO.getInboundEntries();
        List<OutboundEntryDO> outboundEntries = shippingCostEntryVO.getOutboundEntries();

        try {
            String prefix = getPrefix(isInbound);
            int count = shippingCostEntryMapper.countNumberOfEntriesOfToday(prefix);
            String newSerial = MyUtils.formNewSerial(prefix, count, shippingCostEntryDO.getCheckoutDate());

            shippingCostEntryDO.setShippingCostEntrySerial(newSerial);
            shippingCostEntryMapper.insertEntry(shippingCostEntryDO);
            logger.info("Inserted new shipping cost entry, {}", newSerial);

            inboundEntries.forEach(entry -> {
                entry.setShippingCostSerial(newSerial);
                inboundEntryService.updateEntryWithShippingCostSerial(entry);
            });

            outboundEntries.forEach(entry -> {
                entry.setShippingCostSerial(newSerial);
                outboundEntryService.updateEntryWithShippingCostSerial(entry);
            });

            // calculate balance
            accountsService.calculateBalance(shippingCostEntryDO.getPartnerCompanyID());

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("insert failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ShippingCostEntryVO> getEntriesInDateRange(Date startDate, Date endDate,
                                                           int companyID, boolean isInbound) {
        try {
            String prefix = getPrefix(isInbound);

            List<ShippingCostEntryDO> entriesFromDatabase = shippingCostEntryMapper.getEntriesInDateRangeAndParams(
                    MyUtils.dateFormat.format(startDate), MyUtils.dateFormat.format(endDate), companyID, prefix);

            List<ShippingCostEntryVO> entries = new ArrayList<>();
            for (var entryFromDatabase : entriesFromDatabase) {
                ShippingCostEntryVO tempEntry = new ShippingCostEntryVO();
                BeanUtils.copyProperties(entryFromDatabase, tempEntry);

                String serial = tempEntry.getShippingCostEntrySerial();

                List<InboundEntryDO> inboundEntries = inboundEntryService.getEntriesWithShippingCostSerial(serial);
                tempEntry.setInboundEntries(inboundEntries);

                List<OutboundEntryDO> outboundEntries = outboundEntryService.getEntriesWithShippingCostSerial(serial);
                tempEntry.setOutboundEntries(outboundEntries);

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
    public void modifyEntry(ShippingCostEntryVO shippingCostEntryVO) {

        ShippingCostEntryDO modifiedEntry = new ShippingCostEntryDO();
        BeanUtils.copyProperties(shippingCostEntryVO, modifiedEntry);

        List<InboundEntryDO> modifiedInboundEntries = shippingCostEntryVO.getInboundEntries();
        List<OutboundEntryDO> modifiedOutboundEntries = shippingCostEntryVO.getOutboundEntries();

        try {
            String serial = modifiedEntry.getShippingCostEntrySerial();
            ShippingCostEntryDO originEntry = shippingCostEntryMapper.selectEntryBySerialForCompare(serial);

            List<InboundEntryDO> originInboundEntries = inboundEntryService.getEntriesWithShippingCostSerial(serial);
            List<OutboundEntryDO> originOutboundEntries = outboundEntryService.getEntriesWithShippingCostSerial(serial);

            StringBuilder record = new StringBuilder("修改者: " + modifiedEntry.getDrawer() + "; ");
            boolean entryChanged = compareEntryAndFormModificationRecord(record, modifiedEntry, originEntry);

            boolean detailChanged;

            // check for added inbound entry
            List<InboundEntryDO> tempModifiedInboundEntries = new ArrayList<>(modifiedInboundEntries);
            tempModifiedInboundEntries.removeAll(originInboundEntries);
            detailChanged = !tempModifiedInboundEntries.isEmpty();
            for (var entry : tempModifiedInboundEntries) {
                record.append(String.format("增加: %s; ", entry.getInboundEntryID()));
                entry.setShippingCostSerial(serial);
                inboundEntryService.updateEntryWithShippingCostSerial(entry);
            }
            // check for removed inbound entry
            List<InboundEntryDO> tempOriginalInboundEntries = new ArrayList<>(originInboundEntries);
            tempOriginalInboundEntries.removeAll(modifiedInboundEntries);
            detailChanged = detailChanged || !tempOriginalInboundEntries.isEmpty();
            for (var entry : tempOriginalInboundEntries) {
                record.append(String.format("移除: %s; ", entry.getInboundEntryID()));
                entry.setShippingCostSerial("");
                inboundEntryService.updateEntryWithShippingCostSerial(entry);
            }
            // check for added outbound entry
            List<OutboundEntryDO> tempModifiedOutboundEntries = new ArrayList<>(modifiedOutboundEntries);
            tempModifiedOutboundEntries.removeAll(originOutboundEntries);
            detailChanged = detailChanged || !tempModifiedOutboundEntries.isEmpty();
            for (var entry : tempModifiedOutboundEntries) {
                record.append(String.format("增加: %s; ", entry.getOutboundEntryID()));
                entry.setShippingCostSerial(serial);
                outboundEntryService.updateEntryWithShippingCostSerial(entry);
            }
            // check for removed outbound entry
            List<OutboundEntryDO> tempOriginalOutboundEntries = new ArrayList<>(originOutboundEntries);
            tempOriginalOutboundEntries.removeAll(modifiedOutboundEntries);
            detailChanged = detailChanged || !tempOriginalOutboundEntries.isEmpty();
            for (var entry : tempOriginalOutboundEntries) {
                record.append(String.format("移除: %s; ", entry.getOutboundEntryID()));
                entry.setShippingCostSerial("");
                outboundEntryService.updateEntryWithShippingCostSerial(entry);
            }

            if (entryChanged || detailChanged) {
                shippingCostEntryMapper.modifyEntry(modifiedEntry);
                logger.info("Updated shipping cost entry, {}", modifiedEntry.getShippingCostEntrySerial());
                modificationRecordService.insertRecord(modifiedEntry.getShippingCostEntrySerial(), record);
            }
            else {
                logger.warn("Nothing changed, begin rolling back");
                throw new RuntimeException();
            }

            // calculate balance
            accountsService.calculateBalance(shippingCostEntryVO.getPartnerCompanyID());

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @SuppressWarnings("DuplicatedCode")
    private static boolean compareEntryAndFormModificationRecord(StringBuilder record, ShippingCostEntryDO modifiedEntry,
                                                                 ShippingCostEntryDO originEntry) {
        boolean bool = false;
        if (modifiedEntry.getPartnerCompanyID() != originEntry.getPartnerCompanyID()) {
            bool = true;
            record.append(String.format("单位: %s -> %s; ", originEntry.getCompanyAbbreviatedName(),
                    modifiedEntry.getCompanyAbbreviatedName()));
        }
        if (modifiedEntry.getIsTaxDeduction() != originEntry.getIsTaxDeduction()) {
            bool = true;
            record.append(String.format("抵税标志: %d -> %d; ", originEntry.getIsTaxDeduction(),
                    modifiedEntry.getIsTaxDeduction()));
        }
        if (new BigDecimal(modifiedEntry.getTotalAmount())
                .compareTo(new BigDecimal(originEntry.getTotalAmount())) != 0) {
            bool = true;
            record.append(String.format("总运费金额: %s -> %s; ", originEntry.getTotalAmount(),
                    modifiedEntry.getTotalAmount()));
        }
        if (!modifiedEntry.getInvoiceNumber().equals(originEntry.getInvoiceNumber())) {
            bool = true;
            record.append(String.format("发票号码: %s -> %s; ", originEntry.getInvoiceNumber(),
                    modifiedEntry.getInvoiceNumber()));
        }
        if (new BigDecimal(modifiedEntry.getInvoiceAmount())
                .compareTo(new BigDecimal(originEntry.getInvoiceAmount())) != 0) {
            bool = true;
            record.append(String.format("开票金额: %s -> %s; ", originEntry.getInvoiceAmount(),
                    modifiedEntry.getInvoiceAmount()));
        }
        if (!modifiedEntry.getInvoiceDate().equals(originEntry.getInvoiceDate())) {
            bool = true;
            record.append(String.format("开票日期: %s -> %s; ", originEntry.getInvoiceDate(),
                    modifiedEntry.getInvoiceDate()));
        }
        if (!modifiedEntry.getShippingCostType().equals(originEntry.getShippingCostType())) {
            bool = true;
            record.append(String.format("运费标志: %s -> %s; ", originEntry.getShippingCostType(),
                    modifiedEntry.getShippingCostType()));
        }
        if (!modifiedEntry.getRemark().equals(originEntry.getRemark())) {
            bool = true;
            record.append(String.format("备注: %s -> %s;", originEntry.getRemark(), modifiedEntry.getRemark()));
        }

        return bool;
    }

    /* -------------------------- Accounts Stat Service -------------------------- */

    @Transactional(readOnly = true)
    @Override
    public List<Integer> getDistinctCompaniesInvolvedInEntries() {
        try {
            return shippingCostEntryMapper.queryDistinctCompanyIDs();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<AccountsDetailO> getEntryDetails(int companyID, boolean isInbound) {
        try {
            var results = new ArrayList<AccountsDetailO>();
            var list = shippingCostEntryMapper.queryAllEntriesByPrefixAndCompany(
                    isInbound ? SHIPPING_COST_PAY : SHIPPING_COST_RECV, companyID);
            for (var item : list) {
                AccountsDetailO detailO = new AccountsDetailO();
                detailO.setEntryID(item.getShippingCostEntrySerial());
                detailO.setEntryDate(item.getCheckoutDate());
                detailO.setExplanation(MyUtils.getExplanationFromEntry(item));
                if (isInbound) { // 付运
                    detailO.setDebtorAmount(item.getTotalAmount());
                    detailO.setCreditorAmount("");
                }
                else { // 收运
                    detailO.setDebtorAmount("");
                    detailO.setCreditorAmount(item.getTotalAmount());
                }
                detailO.setDebitOrCredit(item.getDebitOrCredit());
                detailO.setBalance(item.getBalance());
                detailO.setAuditAmount(""); // todo
                results.add(detailO);
            }
            return results;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateEntryBalance(AccountsDetailO entry) {
        try {
            shippingCostEntryMapper.updateEntryBalanceBySerial(entry);
            logger.info("Updated shipping cost entry with balance, {}", entry.getEntryID());

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }
}
