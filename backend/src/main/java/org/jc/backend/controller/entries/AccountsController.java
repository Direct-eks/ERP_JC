package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.StatO.AccountsDetailO;
import org.jc.backend.entity.StatO.AccountsSummaryO;
import org.jc.backend.service.AccountsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Indexed
@Api(tags = "Accounts Related")
@RestController
@RequestMapping("/accounts")
public class AccountsController {
    private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);

    private final AccountsService accountsService;

    public AccountsController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    /* ------------------------------ API ------------------------------ */

    @GetMapping("/getPayableSummary")
    public List<AccountsSummaryO> getPayableSummary() {
        logger.info("GET Request to /accounts/getPayableSummary");

        return accountsService.getPayableSummary();
    }

    @GetMapping("/getReceivableSummary")
    public List<AccountsSummaryO> getReceivableSummary() {
        logger.info("GET Request to /accounts/getReceivableSummary");

        return accountsService.getReceivableSummary();
    }

    @GetMapping("/getPayableDetail")
    public List<AccountsDetailO> getPayableDetail(
            @RequestParam("companyID") int companyID
    ) throws GlobalParamException {
        logger.info("GET Request to /accounts/getPayableDetail, id: {}", companyID);

        return accountsService.getPayableDetail(companyID);
    }

    @GetMapping("/getReceivableDetail")
    public List<AccountsDetailO> getReceivableDetail(
            @RequestParam("companyID") int companyID
    )  throws GlobalParamException {
        logger.info("GET Request to /accounts/getReceivableDetail, id: {}", companyID);

        return accountsService.getReceivableDetail(companyID);
    }

    @GetMapping("/getPayableLedger")
    public List<AccountsSummaryO> getPayableLedger() {
        return null;
    }

    @GetMapping("/getReceivableLedger")
    public List<AccountsSummaryO> getReceivableLedger() {
        return null;
    }
}
