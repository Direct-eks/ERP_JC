package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.MoneyEntryO;
import org.jc.backend.service.MoneyEntryService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Api(tags = "MoneyEntry Related")
@RestController
@RequestMapping("/moneyEntry")
public class MoneyEntryController {
    private static final Logger logger = LoggerFactory.getLogger(MoneyEntryController.class);

    private final MoneyEntryService moneyEntryService;

    public MoneyEntryController(MoneyEntryService moneyEntryService) {
        this.moneyEntryService = moneyEntryService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = void.class)
    @PutMapping("/createEntry")
    public void createEntry(@RequestBody @Validated MoneyEntryO moneyEntryO,
                            @RequestParam("isInbound") boolean isInbound) {
        logger.info("PUT Request to /moneyEntry/createEntry");

        moneyEntryService.createEntry(moneyEntryO, isInbound);
    }

    @ApiOperation(value = "", response = MoneyEntryO.class,
            notes = "bankAccount should be 0 when not provided, because default value is -1")
    @GetMapping("/getEntriesInDateRange")
    public List<MoneyEntryO> getEntriesInDateRange(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam(value = "companyID", defaultValue = "-1") int companyID,
            @RequestParam(value = "paymentMethod", defaultValue = "") String paymentMethod,
            @RequestParam(value = "bankAccountID", defaultValue = "0") int bankAccountID,
            @RequestParam(value = "isInbound") boolean isInbound
    ) throws GlobalParamException {
        logger.info("GET Request to /moneyEntry/getEntriesInDateRange, startDate: " + startDateString +
                        ", endDate: " + endDateString + ", companyID: " + companyID + ", paymentMethod: " +
                        paymentMethod + ", bankAccountID: " + bankAccountID);

        Date startDate = MyUtils.parseAndCheckDateString(startDateString);
        Date endDate = MyUtils.parseAndCheckDateString(endDateString);

        if (StringUtils.hasLength(paymentMethod)) {
            switch (paymentMethod) {
                case "现金":
                case "转支":
                case "电汇":
                case "汇票":
                case "其他":
                    break;
                default:
                    throw new GlobalParamException("Invalid invoiceType param");
            }
        }

        return moneyEntryService.getEntriesInDateRange(startDate, endDate, companyID,
                paymentMethod, bankAccountID, isInbound);
    }

    @ApiOperation(value = "", response = MoneyEntryO.class)
    @GetMapping("/getEntryBySerial")
    public List<MoneyEntryO> getEntryBySerial(
            @RequestParam("serial") String serial,
            @RequestParam("isInbound") boolean isInbound
    ) throws GlobalParamException {
        logger.info("GET Request to /moneyEntry/getEntryBySerial");

        String serialSuffix = StringUtils.trimAllWhitespace(serial);

        Pattern pattern = Pattern.compile("^[0-9]{6}$");
        Matcher matcher = pattern.matcher(serialSuffix);
        if (!matcher.matches()) throw new GlobalParamException("Invalid serial", 400);

        return moneyEntryService.getEntryBySerial(serial, isInbound);
    }

    @ApiOperation(value = "", response = void.class)
    @PatchMapping("/modifyEntry")
    public void modifyEntry(@RequestBody @Validated MoneyEntryO moneyEntryO) {
        logger.info("PATCH Request to /moneyEntry/modifyEntry");

        moneyEntryService.modifyEntry(moneyEntryO);
    }
}
