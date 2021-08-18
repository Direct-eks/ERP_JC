package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.FeesMapper;
import org.jc.backend.entity.FeeCategoryO;
import org.jc.backend.service.FeesService;
import org.jc.backend.service.UsageCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeesServiceImpl implements FeesService {
    private static final Logger logger = LoggerFactory.getLogger(FeesServiceImpl.class);

    private final FeesMapper feesMapper;
    private final UsageCheckService usageCheckService;

    public FeesServiceImpl(
            FeesMapper feesMapper,
            UsageCheckService usageCheckService
    ) {
        this.feesMapper = feesMapper;
        this.usageCheckService = usageCheckService;
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
    public void updateFeeCategories(List<FeeCategoryO> updateVO) {
        try {
            List<FeeCategoryO> tempCategories = new ArrayList<>(updateVO);

            tempCategories.removeIf(c -> c.getFeeCategoryID() >= 0);
            for (var c : tempCategories) {
                feesMapper.insertCategory(c);
            }

            tempCategories = new ArrayList<>(updateVO);
            tempCategories.removeIf(c -> c.getFeeCategoryID() < 0);
            for (var c : tempCategories) {
                feesMapper.updateCategory(c);
            }

            List<FeeCategoryO> oldCategories = feesMapper.queryAllCategories();
            oldCategories.removeIf(oldC -> updateVO.stream()
                    .anyMatch(c -> c.getFeeCategoryID().equals(oldC.getFeeCategoryID())));
            for (var c : oldCategories) {
                if (!usageCheckService.isFeeCategoryIDInUse(c.getFeeCategoryID())) {
                    feesMapper.deleteCategory(c.getFeeCategoryID());
                }
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

}
