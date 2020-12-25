package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.config.exception.GlobalException;
import org.jc.backend.entity.InboundEntryCompleteO;
import org.jc.backend.entity.VO.InboundEntryModifyVO;
import org.jc.backend.entity.VO.InboundEntryWithProductsVO;
import org.jc.backend.service.InboundEntryService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Api(tags = "InboundEntry Related")
@RestController
@RequestMapping("/inboundEntry")
public class InboundEntryController {
    private static final Logger logger = LoggerFactory.getLogger(InboundEntryController.class);

    private final InboundEntryService inboundEntryService;

    public InboundEntryController(InboundEntryService inboundEntryService) {
        this.inboundEntryService = inboundEntryService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = void.class)
    @PutMapping("/createEntry")
    public void createEntry(@RequestBody @Validated InboundEntryWithProductsVO inboundEntryVO) throws GlobalException {
        logger.info("PUT Request to /inboundEntry/createEntry");

        inboundEntryService.createEntry(inboundEntryVO);
    }

    @ApiOperation(value = "", response = InboundEntryWithProductsVO.class)
    @GetMapping("/getEntriesInDateRangeByCompanyID")
    public List<InboundEntryWithProductsVO> getEntriesInDateRangeByCompanyID(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam(value = "id", defaultValue = "-1") int id
    ) throws GlobalException {
        logger.info("GET Request to /inboundEntry/getEntriesInDateRangeByCompanyID, start date: " +
                startDateString + ", end date： " + endDateString + ", id: " + id);

        Date startDate = MyUtils.parseAndCheckDateString(startDateString);
        Date endDate = MyUtils.parseAndCheckDateString(endDateString);

        return inboundEntryService.getEntriesInDateRangeByCompanyID(startDate, endDate, id);
    }

    @ApiOperation(value = "", response = void.class)
    @PatchMapping("/completeEntry")
    public void completeEntry(@RequestBody @Validated InboundEntryCompleteO completionO) {
        logger.info("PATCH Request to /inboundEntry/completeEntry");

        inboundEntryService.completeEntry(completionO);
    }

    @ApiOperation(value = "", response = void.class)
    @PatchMapping("/modifyEntry")
    public void modifyEntry(@RequestBody @Validated InboundEntryModifyVO modificationVO) {
        logger.info("PATCH Request to /inboundEntry/modifyEntry");

        inboundEntryService.modifyEntry(modificationVO);
    }

    @ApiOperation(value = "", response = void.class)
    @DeleteMapping("/deleteEntry/{id}")
    public void deleteEntry(@PathVariable("id") String id) {
        logger.info("DELETE Request to /inboundEntry/deleteEntry, id: " + id);

        inboundEntryService.deleteEntry(id);
    }
}
