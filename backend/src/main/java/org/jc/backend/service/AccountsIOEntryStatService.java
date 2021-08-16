package org.jc.backend.service;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Indexed;

@Indexed
public interface AccountsIOEntryStatService {
    // summary
    String getNotCheckoutEntrySummary(int companyID);

    // ledger
    Pair<String, String> getLedgerSummary(int companyID);
}
