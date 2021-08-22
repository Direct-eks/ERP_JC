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

import java.util.ArrayList;
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
    @Override
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
    @Override
    public List<BankAccountO> getVisibleAccounts() {
        try {
            return bankAccountMapper.queryVisibleAccounts();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateAccounts(List<BankAccountO> accountsVO) {
        try {
            List<BankAccountO> tempAccounts = new ArrayList<>(accountsVO);

            // check for added
            tempAccounts.removeIf(a -> a.getBankAccountID() >= 0);
            for (var account : tempAccounts) {
                bankAccountMapper.insertAccount(account);
                logger.info("Inserted bank account: {}", account);
            }

            // update all
            tempAccounts = new ArrayList<>(accountsVO);
            tempAccounts.removeIf(a -> a.getBankAccountID() < 0);
            for (var account :  tempAccounts) {
                bankAccountMapper.updateAccount(account);
                logger.info("Updated bank account: {}", account);
            }

            // check for removed
            List<BankAccountO> oldAccounts = bankAccountMapper.queryAllAccounts();
            oldAccounts.removeIf(oldA -> accountsVO.stream()
                    .anyMatch(a -> a.getBankAccountID().equals(oldA.getBankAccountID())));
            for (var account : oldAccounts) {
                if (!usageCheckService.isBankAccountIDInUse(account.getBankAccountID())) {
                    bankAccountMapper.deleteAccount(account.getBankAccountID());
                    logger.info("Deleted bank account: {}", account);
                }
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }
}
