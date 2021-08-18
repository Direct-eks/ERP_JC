package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.controller.entries.AccountsController;
import org.jc.backend.dao.FeeEntryMapper;
import org.jc.backend.entity.FeeCategoryO;
import org.jc.backend.service.FeeEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FeeEntryServiceImpl implements FeeEntryService {
    private static final Logger logger = LoggerFactory.getLogger(FeeEntryServiceImpl.class);

    private final FeeEntryMapper feeEntryMapper;

    public FeeEntryServiceImpl(FeeEntryMapper feeEntryMapper) {
        this.feeEntryMapper = feeEntryMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional(readOnly = true)
    @Override
    public List<FeeCategoryO> getFeeCategories() {
        try {
            return feeEntryMapper.queryAllCategories();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateFeeCategories(List<FeeCategoryO> categories) {
        try {

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

}
