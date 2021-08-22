package org.jc.backend.service;

import org.jc.backend.entity.BankAccountO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface BankAccountService {
    List<BankAccountO> getAllAccounts();
    List<BankAccountO> getVisibleAccounts();
    void updateAccounts(List<BankAccountO> accountsVO);
}
