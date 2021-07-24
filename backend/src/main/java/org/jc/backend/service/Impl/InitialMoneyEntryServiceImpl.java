package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.InitialMoneyEntryMapper;
import org.jc.backend.entity.InitialMoneyEntryO;
import org.jc.backend.service.InitialMoneyEntryService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InitialMoneyEntryServiceImpl implements InitialMoneyEntryService {
    private static final Logger logger = LoggerFactory.getLogger(InitialMoneyEntryServiceImpl.class);

    private final InitialMoneyEntryMapper initialMoneyEntryMapper;

    public InitialMoneyEntryServiceImpl(InitialMoneyEntryMapper initialMoneyEntryMapper) {
        this.initialMoneyEntryMapper = initialMoneyEntryMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    @Override
    public void createEntry(boolean isInbound, InitialMoneyEntryO newEntry) {
        try {

            String entryDate = newEntry.getEntryDate();
            int count = initialMoneyEntryMapper.countNumberOfEntriesOfGivenDate(entryDate, getPrefix(isInbound));
            MyUtils.formNewSerial(getPrefix(isInbound), count, entryDate);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Insert failed");
            throw e;
        }
    }

    private String getPrefix(boolean isInbound) {
        return isInbound ? "初付" : "初收";
    }

    @Transactional(readOnly = true)
    @Override
    public List<InitialMoneyEntryO> getEntries(boolean isInbound) {
        try {
            return initialMoneyEntryMapper.queryEntries(getPrefix(isInbound));

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public InitialMoneyEntryO getEntryByCompanyID(boolean isInbound, int id) {
        try {
            return initialMoneyEntryMapper.queryEntryByCompanyID(getPrefix(isInbound), id);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void modifyEntries(boolean isInbound, List<InitialMoneyEntryO> updateVO) {
        try {

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }
}
