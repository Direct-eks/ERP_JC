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
@Api(tags = "TransferEntry Related")
@RestController
@RequestMapping("/transferEntry")
public class TransferEntryController {
    private static final Logger logger = LoggerFactory.getLogger(TransferEntryController.class);

    private final WarehouseEntryService transferEntryService;

    private static final String ENTRY_TYPE1 = "调入";
    private static final String ENTRY_TYPE2 = "调出";
    private static final boolean INBOUND1 = true;
    private static final boolean INBOUND2 = false;

    public TransferEntryController(WarehouseEntryService transferEntryService) {
        this.transferEntryService = transferEntryService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = void.class)
    @PutMapping("/createEntry")
    public void createEntry(@RequestBody @Validated WarehouseEntryWithProductsVO entry) throws GlobalParamException {
        logger.info("PUT Request to /transferEntry/createEntry, data: {}", entry);

        // check for quantity, VO validation only check for >= 0 due to quantity
        // becoming 0 after returning.
        for (var p : entry.getEntryProducts()) {
            if (p.getQuantity() < 1) {
                throw new GlobalParamException("商品入库数量必须大于0");
            }
        }

        transferEntryService.createEntry(entry, ENTRY_TYPE1, INBOUND1);
        transferEntryService.createEntry(entry, ENTRY_TYPE2, INBOUND2);
    }

    @ApiOperation(value = "", response = WarehouseEntryWithProductsVO.class)
    @GetMapping("/getEntriesInDateRange")
    public List<WarehouseEntryWithProductsVO> getEntriesInDateRange(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString
    ) throws GlobalParamException {
        logger.info("GET Request to /transferEntry/getEntriesInDateRange, start date: " +
                "{}, end date: {}", startDateString, endDateString);

        Date startDate = MyUtils.parseAndCheckDateString(startDateString);
        Date endDate = MyUtils.parseAndCheckDateString(endDateString);

        transferEntryService.getEntriesInDateRange(startDate, endDate, ENTRY_TYPE1, INBOUND1);
        return transferEntryService.getEntriesInDateRange(startDate, endDate, ENTRY_TYPE2, INBOUND2);
    }

    @ApiOperation(value = "", response = void.class)
    @PatchMapping("/modifyEntry")
    public void modifyEntry(@RequestBody @Validated WarehouseEntryWithProductsVO entry) {
        logger.info("PATCH Request to /transferEntry/modifyEntry, entry: {}", entry);

        transferEntryService.modifyEntry(entry, ENTRY_TYPE1, INBOUND1);
        transferEntryService.modifyEntry(entry, ENTRY_TYPE2, INBOUND2);
    }

}
