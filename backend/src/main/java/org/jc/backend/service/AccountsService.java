package org.jc.backend.service;

import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.StatO.MoneyEntryDetailO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface AccountsService {
    List<MoneyEntryDetailO> getPayableSummary(int companyID) throws GlobalParamException;
    List<MoneyEntryDetailO> getReceivableSummary(int companyID) throws GlobalParamException;
}
