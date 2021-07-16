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
@Api(tags = "MaterialApplyEntry Related")
@RestController
@RequestMapping("/materialApplyEntry")
public class MaterialApplyEntryController {
    private static final Logger logger = LoggerFactory.getLogger(MaterialApplyEntryController.class);

    private final WarehouseEntryService materialApplyEntryService;

    private static final String ENTRY_TYPE = "领料";
    private static final boolean INBOUND = false;

    public MaterialApplyEntryController(WarehouseEntryService materialApplyEntryService) {
        this.materialApplyEntryService = materialApplyEntryService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = void.class)
    @PutMapping("/createEntry")
    public void createEntry(@RequestBody @Validated WarehouseEntryWithProductsVO entry) throws GlobalParamException {
        logger.info("PUT Request to /materialApplyEntry/createEntry, data: {}", entry);

        // check for quantity, VO validation only check for >= 0 due to quantity
        // becoming 0 after returning.
        for (var p : entry.getEntryProducts()) {
            if (p.getQuantity() < 1) {
                throw new GlobalParamException("商品入库数量必须大于0");
            }
        }

        materialApplyEntryService.createEntry(entry, ENTRY_TYPE, INBOUND);
    }

    @ApiOperation(value = "", response = WarehouseEntryWithProductsVO.class)
    @GetMapping("/getEntriesInDateRange")
    public List<WarehouseEntryWithProductsVO> getEntriesInDateRange(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString
    ) throws GlobalParamException {
        logger.info("GET Request to /materialApplyEntry/getEntriesInDateRange, start date: " +
                "{}, end date: {}", startDateString, endDateString);

        Date startDate = MyUtils.parseAndCheckDateString(startDateString);
        Date endDate = MyUtils.parseAndCheckDateString(endDateString);

        return materialApplyEntryService.getEntriesInDateRange(startDate, endDate, ENTRY_TYPE, INBOUND);
    }

    @ApiOperation(value = "", response = void.class)
    @PatchMapping("/modifyEntry")
    public void modifyEntry(@RequestBody @Validated WarehouseEntryWithProductsVO entry) {
        logger.info("PATCH Request to /materialApplyEntry/modifyEntry, entry: {}", entry);

        materialApplyEntryService.modifyEntry(entry, ENTRY_TYPE, INBOUND);
    }

}
