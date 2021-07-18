package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.StatO.InboundSummaryO;
import org.jc.backend.entity.VO.WarehouseEntryWithProductsVO;
import org.jc.backend.service.WarehouseEntryService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Indexed
@Api(tags = "WarehouseEntry Related")
@RestController
@RequestMapping("/warehouseEntry")
public class WarehouseEntryController {
    private static final Logger logger = LoggerFactory.getLogger(WarehouseEntryController.class);

    private final WarehouseEntryService warehouseEntryService;

    public WarehouseEntryController(WarehouseEntryService warehouseEntryService) {
        this.warehouseEntryService = warehouseEntryService;
    }

    /* ------------------------------ API ------------------------------ */

    private static boolean isInbound(String type) {
        switch (type) {
            case "产入":
            case "折入":
            case "调入":
            case "盘盈":
                return true;
            case "领料":
            case "拆出":
            case "调出":
            case "盘亏":
            case "报废":
            default:
                return false;
        }
    }

    @ApiOperation(value = "", response = void.class)
    @PutMapping("/createEntry")
    public void createEntry(
            @RequestParam("type") String type,
            @RequestBody @Validated WarehouseEntryWithProductsVO entry
    ) throws GlobalParamException {
        logger.info("PUT Request to /warehouseEntry/createEntry, type: {}, data: {}", type, entry);

        // check for quantity, VO validation only check for >= 0 due to quantity
        // becoming 0 after returning.
        for (var p : entry.getEntryProducts()) {
            if (p.getQuantity() < 1) {
                throw new GlobalParamException("商品入库数量必须大于0");
            }
        }

        warehouseEntryService.createEntry(entry, type, isInbound(type));
    }

    @ApiOperation(value = "", response = WarehouseEntryWithProductsVO.class)
    @GetMapping("/getEntriesInDateRange")
    public List<WarehouseEntryWithProductsVO> getEntriesInDateRange(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam("type") String type
    ) throws GlobalParamException {
        logger.info("GET Request to /warehouseEntry/getEntriesInDateRange, type: {}, " +
                "start date: {}, end date: {}", type, startDateString, endDateString);

        Date startDate = MyUtils.parseAndCheckDateString(startDateString);
        Date endDate = MyUtils.parseAndCheckDateString(endDateString);

        return warehouseEntryService.getEntriesInDateRange(
                startDate, endDate, type, isInbound(type));
    }

    @ApiOperation(value = "", response = void.class)
    @PatchMapping("/modifyEntry")
    public void modifyEntry(
            @RequestBody @Validated WarehouseEntryWithProductsVO entry,
            @RequestParam("type") String type
    ) {
        logger.info("PATCH Request to /warehouseEntry/modifyEntry, type: {}, " +
                "entry: {}", type, entry);

        warehouseEntryService.modifyEntry(entry, type, isInbound(type));
    }

    @ApiOperation(value = "", response = InboundSummaryO.class)
    @GetMapping("/summary")
    public List<InboundSummaryO> getSummary(
            @RequestParam("type") String type,
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam("categoryID") int categoryID,
            @RequestParam("factoryBrand") String factoryBrand,
            @RequestParam("warehouseID") int warehouseID,
            @RequestParam("departmentID") int departmentID
    ) {
        logger.info("GET Request to /warehouseEntry/summary");

        return null;
    }

}
