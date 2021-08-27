package org.jc.backend.service;

import org.jc.backend.entity.StatO.AccountsDetailO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface AccountsStatService {
    // summary
    List<Integer> getDistinctCompaniesInvolvedInEntries();
    // detail
    List<AccountsDetailO> getEntryDetails(int id, boolean isInbound);
    default void updateEntryBalance(AccountsDetailO entry) {} // no implementation for initialMoneyEntry
}
