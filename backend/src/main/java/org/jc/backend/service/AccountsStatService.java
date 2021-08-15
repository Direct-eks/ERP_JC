package org.jc.backend.service;

import org.jc.backend.entity.StatO.AccountsDetailO;

import java.util.List;

public interface AccountsStatService {
    List<AccountsDetailO> getEntryDetails(int companyID, boolean isInbound);
    default void updateEntryDetail(AccountsDetailO entry) {}
}
