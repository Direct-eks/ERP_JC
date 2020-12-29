package org.jc.backend.service;

import org.jc.backend.entity.BankAccountO;

import java.util.List;

public interface BankAccountService {
    List<BankAccountO> getAllAccounts();
    List<BankAccountO> getVisibleAccounts();
}
