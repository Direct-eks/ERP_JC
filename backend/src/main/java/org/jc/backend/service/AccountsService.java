package org.jc.backend.service;

import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.StatO.AccountsDetailO;
import org.jc.backend.entity.StatO.AccountsLedgerO;
import org.jc.backend.entity.StatO.AccountsSummaryO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface AccountsService {
    List<AccountsSummaryO> getPayableSummary();
    List<AccountsSummaryO> getReceivableSummary();
    List<AccountsDetailO> getPayableDetail(int companyID) throws GlobalParamException;
    List<AccountsDetailO> getReceivableDetail(int companyID) throws GlobalParamException;
    List<AccountsLedgerO> getPayableLedger();
    List<AccountsLedgerO> getReceivableLedger();

    void calculateBalance(int companyID);
}
