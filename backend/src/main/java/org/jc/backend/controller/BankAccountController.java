package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.entity.BankAccountO;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.jc.backend.service.BankAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Indexed
@Api(tags = "BankAccount Related")
@RestController
@RequestMapping("/bankAccount")
public class BankAccountController {

    private static final Logger logger = LoggerFactory.getLogger(BankAccountController.class);

    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = BankAccountO.class)
    @GetMapping("/getAllAccounts")
    public List<BankAccountO> getAllAccounts() {
        logger.info("GET Request to /bankAccount/getAllAccounts");

        return bankAccountService.getAllAccounts();
    }

    @ApiOperation(value = "", response = BankAccountO.class)
    @GetMapping("/getVisibleAccounts")
    public List<BankAccountO> getVisibleAccounts() {
        logger.info("GET Request to /bankAccount/getVisibleAccounts");

        return bankAccountService.getVisibleAccounts();
    }

    @ApiOperation(value = "", response = void.class)
    @PostMapping("/updateAccounts")
    public void updateAccounts(@RequestBody @Validated ListUpdateVO<BankAccountO> updateVO) {
        logger.info("POST Request to /bankAccount/updateAccounts, data: {}", updateVO.getElements());

        bankAccountService.updateAccounts(updateVO.getElements());
    }
}
