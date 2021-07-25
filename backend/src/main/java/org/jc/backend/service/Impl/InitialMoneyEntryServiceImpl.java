package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.dao.InitialMoneyEntryMapper;
import org.jc.backend.entity.InitialMoneyEntryO;
import org.jc.backend.service.InitialMoneyEntryService;
import org.jc.backend.service.MiscellaneousDataService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class InitialMoneyEntryServiceImpl implements InitialMoneyEntryService {
    private static final Logger logger = LoggerFactory.getLogger(InitialMoneyEntryServiceImpl.class);

    private final InitialMoneyEntryMapper initialMoneyEntryMapper;
    private final MiscellaneousDataService miscellaneousDataService;

    public InitialMoneyEntryServiceImpl(
            InitialMoneyEntryMapper initialMoneyEntryMapper,
            MiscellaneousDataService miscellaneousDataService
    ) {
        this.initialMoneyEntryMapper = initialMoneyEntryMapper;
        this.miscellaneousDataService = miscellaneousDataService;
    }

    /* ------------------------------ SERVICE ------------------------------ */

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
    public void updateEntries(boolean isInbound, List<InitialMoneyEntryO> updateVO) throws GlobalParamException {
        try {
            var tempEntries = new ArrayList<>(updateVO);

            // check for new
            tempEntries.removeIf(e -> !e.getInitialMoneyEntrySerial().isBlank());
            var tempEntries2 = new ArrayList<>(updateVO);
            tempEntries2.removeIf(e -> e.getInitialMoneyEntrySerial().isBlank());
            String initialEntryDate = miscellaneousDataService.getInitialEntryDate();
            for (var newEntry : tempEntries) {
                // check for repeated company
                if (tempEntries2.stream().anyMatch(e ->
                        e.getPartnerCompanyID().equals(newEntry.getPartnerCompanyID()))) {
                    throw new GlobalParamException("检测到重复单位：" + newEntry.getAbbreviatedName());
                }
                newEntry.setEntryDate(initialEntryDate);
                int count = initialMoneyEntryMapper.countNumberOfEntriesOfGivenDate(initialEntryDate, getPrefix(isInbound));
                MyUtils.formNewSerial(getPrefix(isInbound), count, initialEntryDate);
                initialMoneyEntryMapper.insertEntry(newEntry);
            }

            // update existing
            tempEntries = tempEntries2;
            for (var entry : tempEntries) {
                initialMoneyEntryMapper.updateEntry(entry);
            }

            // check for removed
            var originalEntries = initialMoneyEntryMapper.queryEntries(getPrefix(isInbound));
            originalEntries.removeIf(oldE -> updateVO.stream()
                    .anyMatch(e -> e.getInitialMoneyEntrySerial().equals(oldE.getInitialMoneyEntrySerial())));
            for (var entry : originalEntries) {
                initialMoneyEntryMapper.deleteEntry(entry.getInitialMoneyEntrySerial());
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }
}
