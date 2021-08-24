package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jc.backend.dao.BankAccountMapper;
import org.jc.backend.entity.BankAccountO;
import org.jc.backend.service.BankAccountService;
import org.jc.backend.service.ModificationRecordService;
import org.jc.backend.service.UsageCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.jc.backend.service.ModificationRecordService.BANK_ACCOUNT;
import static org.jc.backend.service.ModificationRecordService.DELETION_MSG;

@Service
public class BankAccountServiceImpl implements BankAccountService {
    private static final Logger logger = LoggerFactory.getLogger(BankAccountServiceImpl.class);

    private final BankAccountMapper bankAccountMapper;
    private final ModificationRecordService modificationRecordService;
    private final UsageCheckService usageCheckService;

    public BankAccountServiceImpl(
            BankAccountMapper bankAccountMapper,
            ModificationRecordService modificationRecordService,
            UsageCheckService usageCheckService
    ) {
        this.bankAccountMapper = bankAccountMapper;
        this.modificationRecordService = modificationRecordService;
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
        Subject subject = SecurityUtils.getSubject();
        String usernameString = "修改者：" + subject.getPrincipals().getPrimaryPrincipal() + "; ";

        try {
            List<BankAccountO> tempAccounts = new ArrayList<>(accountsVO);
            List<BankAccountO> oldAccounts = bankAccountMapper.queryAllAccounts();

            // check for added
            tempAccounts.removeIf(a -> a.getBankAccountID() >= 0);
            for (var account : tempAccounts) {
                bankAccountMapper.insertAccount(account);
                logger.info("Inserted bank account, id: {}", account.getBankAccountID());
            }

            // update all
            tempAccounts = new ArrayList<>(accountsVO);
            tempAccounts.removeIf(a -> a.getBankAccountID() < 0);
            for (var account :  tempAccounts) {
                StringBuilder record = new StringBuilder(usernameString);
                if (account.formModificationRecord(account.getOldObject(oldAccounts), record)) {
                    bankAccountMapper.updateAccount(account);
                    logger.info("Updated bank account, id: {}", account.getBankAccountID());
                    modificationRecordService.insertRecord(BANK_ACCOUNT, account.getBankAccountID(), record);
                }
            }

            // check for removed
            oldAccounts.removeIf(oldA -> accountsVO.stream()
                    .anyMatch(a -> a.getBankAccountID().equals(oldA.getBankAccountID())));
            for (var account : oldAccounts) {
                if (!usageCheckService.isBankAccountIDInUse(account.getBankAccountID())) {
                    bankAccountMapper.deleteAccount(account.getBankAccountID());
                    logger.info("Deleted bank account, id: {}", account.getBankAccountID());
                    modificationRecordService.insertRecord(BANK_ACCOUNT, account.getBankAccountID(),
                            usernameString + DELETION_MSG + account);
                }
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }
}
