package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.dao.AcceptanceMapper;
import org.jc.backend.entity.AcceptanceEntryO;
import org.jc.backend.entity.StatO.MoneyEntryDetailO;
import org.jc.backend.service.AcceptanceService;
import org.jc.backend.utils.AcceptanceBillClassification;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AcceptanceServiceImpl implements AcceptanceService {

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
                String base = AcceptanceBillClassification.ACCEPTANCE_RECV;
                int count = acceptanceMapper.countNumberOfEntriesOfGivenDate(entryDate, base);
                newSerial = MyUtils.formNewSerial(base, count, entryDate);
                entryO.setClassification(AcceptanceBillClassification.HOLD);
            }
            else {
                String base = AcceptanceBillClassification.ACCEPTANCE_PAY;
                int count = acceptanceMapper.countNumberOfEntriesOfGivenDate(entryDate, base);
                newSerial = MyUtils.formNewSerial(base, count, entryDate);
                if (entryO.getSource().equals("本公司")) {
                    entryO.setClassification(AcceptanceBillClassification.SELF_PAY);
                }
                else {
                    entryO.setClassification(AcceptanceBillClassification.TRANSFER_PAY);
                    // update corresponding acceptance entry
                    var inboundEntry = acceptanceMapper.queryEntryBySerial(entryO.getSourceSerial());
                    if (inboundEntry == null ||
                            !inboundEntry.getClassification().equals(AcceptanceBillClassification.HOLD)) {
                        throw new GlobalParamException("承兑来源单据错误");
                    }
                    acceptanceMapper.updateClassification(inboundEntry.getAcceptanceEntrySerial(),
                            AcceptanceBillClassification.TRANSFERRED);
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
                if (oldEntry.getClassification().equals(AcceptanceBillClassification.TRANSFERRED)) {
                    throw new GlobalParamException("此单据已付出，不能修改");
                }
            }
            else {
                if (oldEntry.getClassification().equals(AcceptanceBillClassification.SELF_PAY) &&
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


    @Transactional(readOnly = true)
    @Override
    public List<MoneyEntryDetailO> getInboundEntryDetails() {
        try {
            return null; //todo

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoneyEntryDetailO> getOutboundEntryDetails() {
        try {
            return null; //todo

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }
}
