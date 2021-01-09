package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.config.exception.GlobalException;
import org.jc.backend.entity.DO.ShippingCostEntryDO;
import org.jc.backend.entity.VO.ShippingCostEntryVO;
import org.jc.backend.service.ShippingCostEntryService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Api(tags = "ShippingCostEntry Related")
@RestController
@RequestMapping("/shippingCostEntry")
public class ShippingCostEntryController {
    private static final Logger logger = LoggerFactory.getLogger(ShippingCostEntryController.class);

    private final ShippingCostEntryService shippingCostEntryService;

    public ShippingCostEntryController(ShippingCostEntryService shippingCostEntryService) {
        this.shippingCostEntryService = shippingCostEntryService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = void.class)
    @PutMapping("/createEntry")
    public void createEntry(@RequestBody @Validated ShippingCostEntryVO shippingCostEntryVO,
                            @RequestParam("isInbound") boolean isInbound) {
        logger.info("PUT Request to /shippingCostEntry/createEntry, isInbound: " + isInbound);

        shippingCostEntryService.createEntry(shippingCostEntryVO, isInbound);
    }

    @ApiOperation(value = "", response = ShippingCostEntryVO.class)
    @GetMapping("/getEntriesInDateRange")
    public List<ShippingCostEntryVO> getEntriesInDateRange(
            @RequestParam("isInbound") boolean isInbound,
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam(value = "companyID", defaultValue = "-1") int companyID,
            @RequestParam("forModify") boolean forModify
    ) throws GlobalException {
        logger.info("GET Request to /shippingCostEntry/getEntriesInDateRange, isInbound: " + isInbound +
                ", startDate: " + startDateString + ", endDate: " + endDateString + ", companyID: " + companyID +
                ", forModify: " + forModify);

        Date startDate = MyUtils.parseAndCheckDateString(startDateString);
        Date endDate = MyUtils.parseAndCheckDateString(endDateString);

        List<ShippingCostEntryVO> entries = shippingCostEntryService.getEntriesInDateRange(startDate, endDate,
                companyID, isInbound);

//        if (forModify) {
//            entries.removeIf(entry -> {});
//        }

        return entries;
    }

    @ApiOperation(value = "", response = void.class)
    @PatchMapping("/modifyEntry")
    public void modifyEntry(@RequestBody @Validated ShippingCostEntryVO shippingCostEntryVO) {
        logger.info("PATCH Request to /shippingCostEntry/modifyEntry");

        shippingCostEntryService.modifyEntry(shippingCostEntryVO);
    }

}
