package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.jc.backend.entity.VO.WarehouseEntryWithProductsVO;
import org.jc.backend.service.WarehouseEntryService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Indexed
@Api(tags = "WarehouseDuelEntry Related")
@RestController
@RequestMapping("/warehouseDuelEntry")
public class WarehouseDuelEntryController {
    private static final Logger logger = LoggerFactory.getLogger(WarehouseDuelEntryController.class);

    private final WarehouseEntryService assemblyEntryService;

    public WarehouseDuelEntryController(WarehouseEntryService assemblyEntryService) {
        this.assemblyEntryService = assemblyEntryService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = void.class)
    @PutMapping("/createEntry")
    public void createEntry(
            @RequestParam("type") String type,
            @RequestBody @Validated ListUpdateVO<WarehouseEntryWithProductsVO> entries
    ) throws GlobalParamException {
        logger.info("PUT Request to /warehouseDuelEntry/createEntry, type: {}, data: {}",
                type, entries.getElements());

        // check for quantity, VO validation only check for >= 0 due to quantity
        // becoming 0 after returning.
        for (var e : entries.getElements()) {
            for (var p : e.getEntryProducts()) {
                if (p.getQuantity() < 1) {
                    throw new GlobalParamException("商品入库数量必须大于0");
                }
            }
        }

        if (entries.getElements().size() != 2) throw new GlobalParamException("elements size error");

        // todo related entry serial
        assemblyEntryService.createEntry(entries.getElements().get(0), type + "出", false);
        assemblyEntryService.createEntry(entries.getElements().get(1), type + "入", true);
    }

    @ApiOperation(value = "", response = WarehouseEntryWithProductsVO.class)
    @GetMapping("/getEntriesInDateRange")
    public WarehouseEntryWithProductsVO[][] getEntriesInDateRange(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam("type") String type
    ) throws GlobalParamException {
        logger.info("GET Request to /warehouseDuelEntry/getEntriesInDateRange, type: {}," +
                "start date: {}, end date: {}", type, startDateString, endDateString);

        Date startDate = MyUtils.parseAndCheckDateString(startDateString);
        Date endDate = MyUtils.parseAndCheckDateString(endDateString);

        var list1 = assemblyEntryService.getEntriesInDateRange(startDate, endDate, type + "出", false);
        var list2 = assemblyEntryService.getEntriesInDateRange(startDate, endDate, type + "入", true);
        WarehouseEntryWithProductsVO[][] results = new WarehouseEntryWithProductsVO[list1.size()][2];
        for (int i = 0; i < list1.size(); ++i) {
            results[i][0] = list1.get(i);
            results[i][1] = list2.get(i);
        }
        return results;
    }

    @ApiOperation(value = "", response = void.class)
    @PatchMapping("/modifyEntry")
    public void modifyEntry(
            @RequestParam("type") String type,
            @RequestBody @Validated ListUpdateVO<WarehouseEntryWithProductsVO> entries
    ) {
        logger.info("PATCH Request to /warehouseDuelEntry/modifyEntry, type: {}, entry: {}",
                type, entries.getElements());

        assemblyEntryService.modifyEntry(entries.getElements().get(0), type + "出", false);
        assemblyEntryService.modifyEntry(entries.getElements().get(1), type + "入", true);
    }

}
