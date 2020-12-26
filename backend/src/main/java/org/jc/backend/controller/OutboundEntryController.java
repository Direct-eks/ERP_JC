package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.config.exception.GlobalException;
import org.jc.backend.entity.OutboundEntryCompleteO;
import org.jc.backend.entity.VO.OutboundEntryModifyVO;
import org.jc.backend.entity.VO.OutboundEntryWithProductsVO;
import org.jc.backend.service.OutboundEntryService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Api(tags = "OutboundEntry Related")
@RestController
@RequestMapping("/outboundEntry")
public class OutboundEntryController {
    private static final Logger logger = LoggerFactory.getLogger(OutboundEntryController.class);

    private final OutboundEntryService outboundEntryService;

    public OutboundEntryController(OutboundEntryService outboundEntryService) {
        this.outboundEntryService = outboundEntryService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = void.class)
    @PutMapping("/createEntry")
    public void createEntry(@RequestBody @Validated OutboundEntryWithProductsVO inboundEntryVO) throws GlobalException {
        logger.info("PUT Request to /outboundEntry/createEntry");

        outboundEntryService.createEntry(inboundEntryVO);
    }

    @ApiOperation(value = "", response = OutboundEntryWithProductsVO.class)
    @GetMapping("/getEntriesInDateRangeByCompanyID")
    public List<OutboundEntryWithProductsVO> getEntriesInDateRangeByCompanyID(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam(value = "companyID", defaultValue = "-1") int companyID,
            @RequestParam("type") String type
    ) throws GlobalException {
        logger.info("GET Request to /outboundEntry/getEntriesInDateRangeByCompanyID, start date: " +
                startDateString + ", end date： " + endDateString + ", companyID: " + companyID);

        Date startDate = MyUtils.parseAndCheckDateString(startDateString);
        Date endDate = MyUtils.parseAndCheckDateString(endDateString);

        switch (type) {
            case "销出":
            case "入退":
                break;
            default:
                throw new GlobalException("Invalid type error");
        }

        return outboundEntryService.getEntriesInDateRangeByTypeAndCompanyID(startDate, endDate, type, companyID);
    }

    @ApiOperation(value = "", response = void.class)
    @PatchMapping("/completeEntry")
    public void completeEntry(@RequestBody @Validated OutboundEntryCompleteO completionO) {
        logger.info("PATCH Request to /outboundEntry/completeEntry");

        outboundEntryService.completeEntry(completionO);
    }

    @ApiOperation(value = "", response = void.class)
    @PatchMapping("/modifyEntry")
    public void modifyEntry(@RequestBody @Validated OutboundEntryModifyVO modificationVO) {
        logger.info("PATCH Request to /outboundEntry/modifyEntry");

        outboundEntryService.modifyEntry(modificationVO);
    }

    @ApiOperation(value = "", response = void.class)
    @DeleteMapping("/deleteEntry/{id}")
    public void deleteEntry(@PathVariable("id") String id) {
        logger.info("DELETE Request to /outboundEntry/deleteEntry, id: " + id);

        outboundEntryService.deleteEntry(id);
    }
}
