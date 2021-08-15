package org.jc.backend.service;

import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.StatO.AccountsDetailO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface AccountsService {
    List<AccountsDetailO> getPayableDetail(int companyID) throws GlobalParamException;
    List<AccountsDetailO> getReceivableDetail(int companyID) throws GlobalParamException;
    void calculateBalance(int companyID);
}
