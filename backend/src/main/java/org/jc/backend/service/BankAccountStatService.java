package org.jc.backend.service;

import org.jc.backend.entity.StatO.BankStatO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface BankAccountStatService {
    List<Integer> getDistinctBankAccountsInvolvedInEntries();
    List<BankStatO> getFeeDetails(int bankAccountID, boolean isSolutionPay);
    default void updateEntryBalance(BankStatO statO) {} // only implemented by feeEntry service
}
