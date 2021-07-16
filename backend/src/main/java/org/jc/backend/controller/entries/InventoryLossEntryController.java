package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.config.exception.GlobalParamException;
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
@Api(tags = "InventoryLossEntry Related")
@RestController
@RequestMapping("/inventoryLossEntry")
public class InventoryLossEntryController {
    private static final Logger logger = LoggerFactory.getLogger(InventoryLossEntryController.class);

    private final WarehouseEntryService inventoryLossEntryService;

    private static final String ENTRY_TYPE = "盘亏";
    private static final boolean INBOUND = false;

    public InventoryLossEntryController(WarehouseEntryService inventoryLossEntryService) {
        this.inventoryLossEntryService = inventoryLossEntryService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = void.class)
    @PutMapping("/createEntry")
    public void createEntry(@RequestBody @Validated WarehouseEntryWithProductsVO entry) throws GlobalParamException {
        logger.info("PUT Request to /inventoryLossEntry/createEntry, data: {}", entry);

        // check for quantity, VO validation only check for >= 0 due to quantity
        // becoming 0 after returning.
        for (var p : entry.getEntryProducts()) {
            if (p.getQuantity() < 1) {
                throw new GlobalParamException("商品入库数量必须大于0");
            }
        }

        inventoryLossEntryService.createEntry(entry, ENTRY_TYPE, INBOUND);
    }

    @ApiOperation(value = "", response = WarehouseEntryWithProductsVO.class)
    @GetMapping("/getEntriesInDateRange")
    public List<WarehouseEntryWithProductsVO> getEntriesInDateRange(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString
    ) throws GlobalParamException {
        logger.info("GET Request to /inventoryLossEntry/getEntriesInDateRange, start date: " +
                "{}, end date: {}", startDateString, endDateString);

        Date startDate = MyUtils.parseAndCheckDateString(startDateString);
        Date endDate = MyUtils.parseAndCheckDateString(endDateString);

        return inventoryLossEntryService.getEntriesInDateRange(startDate, endDate, ENTRY_TYPE, INBOUND);
    }

    @ApiOperation(value = "", response = void.class)
    @PatchMapping("/modifyEntry")
    public void modifyEntry(@RequestBody @Validated WarehouseEntryWithProductsVO entry) {
        logger.info("PATCH Request to /inventoryLossEntry/modifyEntry, entry: {}", entry);

        inventoryLossEntryService.modifyEntry(entry, ENTRY_TYPE, INBOUND);
    }

}
