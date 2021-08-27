package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.FeeCategoryO;
import org.jc.backend.entity.VO.FeeEntryWithDetailVO;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.jc.backend.service.FeesService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.jc.backend.utils.EntryClassification.*;

@Indexed
@Api(tags = "Fees Related")
@RestController
@RequestMapping("/fees")
public class FeesController {
    private static final Logger logger = LoggerFactory.getLogger(FeesController.class);

    private final FeesService feesService;

    public FeesController(FeesService feesService) {
        this.feesService = feesService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = FeeCategoryO.class)
    @GetMapping("/getFeeCategories")
    public List<FeeCategoryO> getFeeCategories() {
        logger.info("/fees/getFeeCategories");

        return feesService.getFeeCategories();
    }

    @ApiOperation(value = "", response = void.class)
    @PostMapping("/updateFeeCategories")
    public void updateFeeCategories(
            @RequestBody @Validated ListUpdateVO<FeeCategoryO> updateVO) {
        logger.info("/fees/updateFeeCategories");

        feesService.updateFeeCategories(updateVO.getElements());
    }

    @ApiOperation(value = "", response = void.class)
    @PutMapping("/createEntry")
    public void createEntry(
            @RequestBody @Validated FeeEntryWithDetailVO entryWithDetailVO,
            @RequestParam("prefix") String prefix
    ) throws GlobalParamException {
        logger.info("PUT Request to /fees/createEntry, prefix: {} data: {}",
                prefix, entryWithDetailVO);

        String serialPrefix;
        boolean hasDetail = false;
        switch (prefix) {
            case "bank":
                if (entryWithDetailVO.getSourceAccountID() == null ||
                        entryWithDetailVO.getDestinationAccountID() == null) {
                    throw new GlobalParamException("at least one of the bank id null error");
                }
                serialPrefix = FEE_BANK;
                break;
            case "income":
                if (entryWithDetailVO.getDestinationAccountID() == null ||
                        entryWithDetailVO.getFeeDetails().size() == 0) {
                    throw new GlobalParamException("bank id null error");
                }
                serialPrefix = FEE_INCOME;
                hasDetail = true;
                break;
            case "expenditure":
                if (entryWithDetailVO.getSourceAccountID() == null ||
                        entryWithDetailVO.getFeeDetails().size() == 0) {
                    throw new GlobalParamException("bank id null error");
                }
                serialPrefix = FEE_EXPENDITURE;
                hasDetail = true;
                break;
            case "salary":
                if (entryWithDetailVO.getSourceAccountID() == null) {
                    throw new GlobalParamException("bank id null error");
                }
                serialPrefix = FEE_SALARY;
                break;
            default: throw new GlobalParamException("incorrect prefix");
        }

        feesService.createEntry(entryWithDetailVO, serialPrefix, hasDetail);
    }

    @ApiOperation(value = "", response = FeeEntryWithDetailVO.class)
    @GetMapping("/getEntriesInDateRange")
    public List<FeeEntryWithDetailVO> getEntriesInDateRange(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam(value = "prefix") String prefix
    ) throws GlobalParamException {

        MyUtils.parseAndCheckDateString(startDateString);
        MyUtils.parseAndCheckDateString(endDateString);

        String serialPrefix;
        boolean hasDetail = false;
        switch (prefix) {
            case "bank":
                serialPrefix = FEE_BANK;
                break;
            case "income":
                serialPrefix = FEE_INCOME;
                hasDetail = true;
                break;
            case "expenditure":
                serialPrefix = FEE_EXPENDITURE;
                hasDetail = true;
                break;
            case "salary":
                serialPrefix = FEE_SALARY;
                break;
            default: throw new GlobalParamException("incorrect prefix");
        }

        return feesService.getEntriesInDateRange(startDateString, endDateString, serialPrefix, hasDetail);
    }

    @ApiOperation(value = "", response = void.class)
    @PostMapping("/updateEntry")
    public void updateEntry(
            @RequestBody @Validated FeeEntryWithDetailVO entryWithDetailVO,
            @RequestParam("prefix") String prefix
    ) throws GlobalParamException {
        logger.info("POST Request to /fees/updateEntry, prefix: {}, data {}",
                prefix, entryWithDetailVO);

        boolean containsDetail;
        switch (prefix) {
            case "bank":
            case "salary":
                containsDetail = false;
                break;
            case "income":
            case "expenditure":
                containsDetail = true;
                break;
            default: throw new GlobalParamException("incorrect prefix");
        }

        feesService.updateEntry(entryWithDetailVO, containsDetail);
    }

    
}
