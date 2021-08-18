package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.FeesMapper;
import org.jc.backend.entity.FeeCategoryO;
import org.jc.backend.service.FeesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FeesServiceImpl implements FeesService {
    private static final Logger logger = LoggerFactory.getLogger(FeesServiceImpl.class);

    private final FeesMapper feesMapper;

    public FeesServiceImpl(FeesMapper feesMapper) {
        this.feesMapper = feesMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional(readOnly = true)
    @Override
    public List<FeeCategoryO> getFeeCategories() {
        try {
            return feesMapper.queryAllCategories();

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
