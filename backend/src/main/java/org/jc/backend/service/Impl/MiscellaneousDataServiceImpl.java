package org.jc.backend.service.Impl;


import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.dao.MiscellaneousDataMapper;
import org.jc.backend.entity.MiscellaneousDataO;
import org.jc.backend.service.MiscellaneousDataService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MiscellaneousDataServiceImpl implements MiscellaneousDataService {
    private static final Logger logger = LoggerFactory.getLogger(MiscellaneousDataServiceImpl.class);

    private final MiscellaneousDataMapper miscellaneousDataMapper;

    public MiscellaneousDataServiceImpl(MiscellaneousDataMapper miscellaneousDataMapper) {
        this.miscellaneousDataMapper = miscellaneousDataMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional(readOnly = true)
    @Override
    public List<String> getAllTaxRateOptions() {
        try {
            List<MiscellaneousDataO> taxRates = miscellaneousDataMapper.queryAllTaxRateOptions();
            return taxRates.stream().map(MiscellaneousDataO::getPropertyValue).collect(Collectors.toList());

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public String getLastBackupTime() {
        try {
            return miscellaneousDataMapper.queryLastBackupTime();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateLastBackupTime() {
        try {
            miscellaneousDataMapper.updateLastBackupTime();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void backupDatabase() throws GlobalParamException {
        try {
            MyUtils.databaseBackup();
            this.updateLastBackupTime();

        } catch (IOException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("backup failed");
            throw new GlobalParamException("备份失败");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public String getPermittedRoundingAmountByUser(String username) {
        try {
            return miscellaneousDataMapper.queryPermittedRoundingAmountByUser(username);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void insertPermittedRoundingAmountByUser(String username, String amount) {
        try {
            miscellaneousDataMapper.insertPermittedRoundingAmountByUser(username, amount);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updatePermittedRoundingAmountByUser(String username, String amount) {
        try {
            miscellaneousDataMapper.updatePermittedRoundingAmountByUser(username, amount);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void deletePermittedRoundingAmountByUser(String username) {
        try {
            miscellaneousDataMapper.deletePermittedRoundingAmountByUser(username);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<MiscellaneousDataO> queryAuditMonths() {
        try {
            return miscellaneousDataMapper.queryAuditMonths();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void addNewAuditMonth(String month, String value) {
        try {
            for (var item : miscellaneousDataMapper.queryAuditMonths()) {
                if (item.getPropertyValue().equals(month) &&
                        item.getPropertyValue2().equals(value)) return;
            }
            miscellaneousDataMapper.insertAuditMonth(month, value);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void deleteAuditMonth(String month, String value) {
        try {
            miscellaneousDataMapper.deleteAuditMonth(month, value);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }
}
