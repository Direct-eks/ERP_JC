package org.jc.backend.service.Impl;

import org.jc.backend.dao.BankAccountMapper;
import org.jc.backend.entity.BankAccountO;
import org.jc.backend.service.BankAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BankAccountServiceImpl implements BankAccountService {
    private static final Logger logger = LoggerFactory.getLogger(BankAccountServiceImpl.class);

    private final BankAccountMapper bankAccountMapper;

    public BankAccountServiceImpl(BankAccountMapper bankAccountMapper) {
        this.bankAccountMapper = bankAccountMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional(readOnly = true)
    public List<BankAccountO> getAllAccounts() {
        return bankAccountMapper.queryAllAccounts();
    }

    @Transactional(readOnly = true)
    public List<BankAccountO> getVisibleAccounts() {
        return bankAccountMapper.queryVisibleAccounts();
    }
}
