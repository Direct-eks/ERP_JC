package org.jc.backend.service.Impl;

import org.jc.backend.service.AccountsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AccountsServiceImplTest {

    @Autowired
    AccountsService accountsService;

    @Test
    void balanceCalculation() {
        var list = accountsService.getPayableSummary();
        for (var item : list) {
            accountsService.calculateBalance(item.getCompanyID());
        }
        list = accountsService.getReceivableSummary();
        for (var item : list) {
            accountsService.calculateBalance(item.getCompanyID());
        }
    }
}