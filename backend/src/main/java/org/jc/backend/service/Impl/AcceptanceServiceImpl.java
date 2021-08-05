package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.dao.AcceptanceMapper;
import org.jc.backend.entity.AcceptanceEntryO;
import org.jc.backend.entity.StatO.MoneyEntryDetailO;
import org.jc.backend.service.AcceptanceService;
import org.jc.backend.service.AccountsStatService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.jc.backend.utils.AcceptanceBillClassification.*;

@Service
public class AcceptanceServiceImpl implements AcceptanceService, AccountsStatService {

    private static final Logger logger = LoggerFactory.getLogger(AcceptanceServiceImpl.class);

    private final AcceptanceMapper acceptanceMapper;

    public AcceptanceServiceImpl(AcceptanceMapper acceptanceMapper) {
        this.acceptanceMapper = acceptanceMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    @Override
    public void createEntry(boolean isInbound, AcceptanceEntryO entryO) throws GlobalParamException {
        try {
            String entryDate = entryO.getEntryDate();
            String newSerial;
            if (isInbound) {
                String base = ACCEPTANCE_RECV;
                int count = acceptanceMapper.countNumberOfEntriesOfGivenDate(entryDate, base);
                newSerial = MyUtils.formNewSerial(base, count, entryDate);
                entryO.setClassification(HOLD);
            }
            else {
                String base = ACCEPTANCE_PAY;
                int count = acceptanceMapper.countNumberOfEntriesOfGivenDate(entryDate, base);
                newSerial = MyUtils.formNewSerial(base, count, entryDate);
                if (entryO.getSource().equals("本公司")) {
                    entryO.setClassification(SELF_PAY);
                }
                else {
                    entryO.setClassification(TRANSFER_PAY);
                    // update corresponding acceptance entry
                    var inboundEntry = acceptanceMapper.queryEntryBySerial(entryO.getSourceSerial());
                    if (inboundEntry == null ||
                            !inboundEntry.getClassification().equals(HOLD)) {
                        throw new GlobalParamException("承兑来源单据错误");
                    }
                    acceptanceMapper.updateClassification(inboundEntry.getAcceptanceEntrySerial(),
                            TRANSFERRED);
                }
            }
            entryO.setAcceptanceEntrySerial(newSerial);
            acceptanceMapper.insertEntry(entryO);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("insert failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void createSolutionPayEntry() {
        try {

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("insert failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<AcceptanceEntryO> getEntryByNumber(String number) {
        try {
            return acceptanceMapper.queryEntryByNumber(number);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<AcceptanceEntryO> getEntriesInDateRange(String startDate, String endDate, String prefix) {
        try {
            return acceptanceMapper.queryEntriesInDateRange(startDate, endDate, prefix);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateEntry(boolean isInbound, AcceptanceEntryO entryO) throws GlobalParamException {
        try {
            var oldEntry = acceptanceMapper.queryEntryBySerial(entryO.getAcceptanceEntrySerial());

            if (isInbound) {
                if (oldEntry.getClassification().equals(TRANSFERRED)) {
                    throw new GlobalParamException("此单据已付出，不能修改");
                }
            }
            else {
                if (oldEntry.getClassification().equals(SELF_PAY) &&
                        !oldEntry.getSourceSerial().isBlank()) {
                    throw new GlobalParamException("此单据已承付，不能修改");
                }
            }

            // todo

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    /* -------------------------- Accounts Stat Service -------------------------- */

    @Transactional(readOnly = true)
    @Override
    public List<MoneyEntryDetailO> getEntryDetails(int companyID, boolean isInbound) {
        try {
            var results = new ArrayList<MoneyEntryDetailO>();
            var list = acceptanceMapper.queryAllEntriesByPrefixAndCompany(isInbound ?
                    ACCEPTANCE_RECV : ACCEPTANCE_PAY, companyID);
            for (var item : list) {
                MoneyEntryDetailO detailO = new MoneyEntryDetailO();
                detailO.setEntryID(item.getAcceptanceEntrySerial());
                detailO.setEntryDate(item.getEntryDate());
                detailO.setExplanation(MyUtils.getExplanationFromEntry(item));
                detailO.setDebtorAmount("");
                detailO.setCreditorAmount("");
                detailO.setAuditAmount(""); // todo
                detailO.setAmount(item.getAmount());
                detailO.setDebitOrCredit(item.getDebitOrCredit());
                results.add(detailO);
            }

            return results;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateEntryDetail(MoneyEntryDetailO entry) {
        try {

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }
}
