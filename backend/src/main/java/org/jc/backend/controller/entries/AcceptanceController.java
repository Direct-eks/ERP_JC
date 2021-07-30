package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.AcceptanceEntryO;
import org.jc.backend.service.AcceptanceService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Indexed
@Api(tags = "Acceptance Entry Related")
@RestController
@RequestMapping("/acceptanceEntry")
public class AcceptanceController {

    private static final Logger logger = LoggerFactory.getLogger(AcceptanceController.class);

    private final AcceptanceService acceptanceService;

    public AcceptanceController(AcceptanceService acceptanceService) {
        this.acceptanceService = acceptanceService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = void.class)
    @PutMapping("/createEntry")
    public void createEntry(
            @RequestBody @Validated AcceptanceEntryO entryO,
            @RequestParam("isInbound") boolean isInbound
    ) throws GlobalParamException {
        logger.info("PUT Request to /acceptanceEntry/createEntry, data: {}", entryO);

        if (!isInbound) {
            switch (entryO.getSource()) {
                case "本公司":
                case "外公司":
                    break;
                default:
                    throw new GlobalParamException("来源错误");
            }
        }

        acceptanceService.createEntry(isInbound, entryO);
    }

    @ApiOperation(value = "", response = void.class)
    @PutMapping("/createSolutionPayEntry")
    public void createSolutionPayEntry() {
        logger.info("PUT Request to /acceptanceEntry/createSolutionPayEntry");

    }

    @ApiOperation(value = "", response = AcceptanceEntryO.class)
    @GetMapping("/getEntriesByNumber")
    public AcceptanceEntryO getEntriesByNumber() {
        return null;
    }

    @ApiOperation(value = "", response = AcceptanceEntryO.class)
    @GetMapping("/getEntriesInDateRange")
    public List<AcceptanceEntryO> getEntriesInDateRange(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam("classification") String classification
    ) throws GlobalParamException {
        logger.info("GET Request to /acceptanceEntry/getEntriesInDateRange, " +
                "startDate: {}, endDate: {}, classification: {}", startDateString,
                endDateString, classification);

        MyUtils.parseAndCheckDateString(startDateString);
        MyUtils.parseAndCheckDateString(endDateString);

        return acceptanceService.getEntriesInDateRange(startDateString, endDateString, classification);
    }
}
