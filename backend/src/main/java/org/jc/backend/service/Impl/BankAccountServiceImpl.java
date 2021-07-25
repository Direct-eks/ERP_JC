package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.BankAccountMapper;
import org.jc.backend.entity.BankAccountO;
import org.jc.backend.service.BankAccountService;
import org.jc.backend.service.UsageCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BankAccountServiceImpl implements BankAccountService {
    private static final Logger logger = LoggerFactory.getLogger(BankAccountServiceImpl.class);

    private final BankAccountMapper bankAccountMapper;
    private final UsageCheckService usageCheckService;

    public BankAccountServiceImpl(
            BankAccountMapper bankAccountMapper,
            UsageCheckService usageCheckService
    ) {
        this.bankAccountMapper = bankAccountMapper;
        this.usageCheckService = usageCheckService;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional(readOnly = true)
    public List<BankAccountO> getAllAccounts() {
        try {
            return bankAccountMapper.queryAllAccounts();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<BankAccountO> getVisibleAccounts() {
        try {
            return bankAccountMapper.queryVisibleAccounts();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }
}
