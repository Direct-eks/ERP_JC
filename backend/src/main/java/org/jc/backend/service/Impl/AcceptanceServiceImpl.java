package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.AcceptanceMapper;
import org.jc.backend.entity.AcceptanceEntryO;
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
    public void createEntry(boolean isInbound, AcceptanceEntryO entryO) {
        try {
            String entryDate = entryO.getEntryDate();
            String newSerial;
            if (isInbound) {
                String base = AcceptanceBillClassification.ACCEPTANCE_RECV;
                int count = acceptanceMapper.countNumberOfEntriesOfGivenDate(entryDate, base);
                newSerial = MyUtils.formNewSerial(base, count, entryDate);
            }
            else {
                String base = AcceptanceBillClassification.ACCEPTANCE_PAY;
                int count = acceptanceMapper.countNumberOfEntriesOfGivenDate(entryDate, base);
                newSerial = MyUtils.formNewSerial(base, count, entryDate);
            }
            entryO.setAcceptanceEntrySerial(newSerial);
            entryO.setClassification(AcceptanceBillClassification.HOLD);
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
    public AcceptanceEntryO getEntryByNumber(String number) {
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
}
