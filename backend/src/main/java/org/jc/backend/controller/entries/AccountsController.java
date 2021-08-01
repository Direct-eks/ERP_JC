package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.StatO.MoneyEntryDetailO;
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
    public List<MoneyEntryDetailO> getPayableSummary(
            @RequestParam("companyID") int companyID
    ) throws GlobalParamException {
        logger.info("GET Request to /accounts/getPayableSummary");

        return accountsService.getPayableSummary(companyID);
    }

    @GetMapping("/getReceivableSummary")
    public List<MoneyEntryDetailO> getReceivableSummary(
            @RequestParam("companyID") int companyID
    )  throws GlobalParamException {
        logger.info("GET Request to /accounts/getReceivableSummary");

        return accountsService.getReceivableSummary(companyID);
    }

}
