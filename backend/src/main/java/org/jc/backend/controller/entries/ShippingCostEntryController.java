package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.VO.ShippingCostEntryVO;
import org.jc.backend.service.ShippingCostEntryService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Indexed
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
    @RequiresPermissions(value = {"inboundShippingCost:Creation", "outboundShippingCost:Creation"},
            logical = Logical.OR)
    @PutMapping("/createEntry")
    public void createEntry(@RequestBody @Validated ShippingCostEntryVO shippingCostEntryVO,
                            @RequestParam("isInbound") boolean isInbound) {
        logger.info("PUT Request to /shippingCostEntry/createEntry, isInbound: {}, data: {}", isInbound,
                shippingCostEntryVO.toString());

        shippingCostEntryService.createEntry(shippingCostEntryVO, isInbound);
    }

    @ApiOperation(value = "", response = ShippingCostEntryVO.class)
    @RequiresPermissions(value = {"inboundShippingCost:Query", "outboundShippingCost:Query"},
            logical = Logical.OR)
    @GetMapping("/getEntriesInDateRange")
    public List<ShippingCostEntryVO> getEntriesInDateRange(
            @RequestParam("isInbound") boolean isInbound,
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam(value = "companyID", defaultValue = "-1") int companyID,
            @RequestParam("forModify") boolean forModify
    ) throws GlobalParamException {
        logger.info("GET Request to /shippingCostEntry/getEntriesInDateRange, isInbound: {}; " +
                        "startDate: {}; endDate: {}; companyID: {}; forModify: {}",
                isInbound, startDateString, endDateString, companyID, forModify);

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
    @RequiresPermissions(value = {"inboundShippingCost:Modification", "outboundShippingCost:Modification"},
            logical = Logical.OR)
    @PatchMapping("/modifyEntry")
    public void modifyEntry(@RequestBody @Validated ShippingCostEntryVO shippingCostEntryVO) {
        logger.info("PATCH Request to /shippingCostEntry/modifyEntry, data: {}", shippingCostEntryVO.toString());

        shippingCostEntryService.modifyEntry(shippingCostEntryVO);
    }

}
