package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.VO.WarehouseEntryWithProductsVO;
import org.jc.backend.service.WarehouseEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Indexed
@Api(tags = "WarehouseEntry Related")
@RestController
@RequestMapping("/warehouseEntry")
public class ProductionEntryController {
    private static final Logger logger = LoggerFactory.getLogger(ProductionEntryController.class);

    private final WarehouseEntryService warehouseEntryService;

    public ProductionEntryController(WarehouseEntryService warehouseEntryService) {
        this.warehouseEntryService = warehouseEntryService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = void.class)
    @PutMapping("/createEntry")
    public void createEntry(@RequestBody @Validated WarehouseEntryWithProductsVO entry) throws GlobalParamException {
        logger.info("PUT Request to /warehouseEntry/createEntry, data: {}", entry);

        // check for quantity, VO validation only check for >= 0 due to quantity
        // becoming 0 after returning.
        for (var p : entry.getEntryProducts()) {
            if (p.getQuantity() < 1) {
                throw new GlobalParamException("商品入库数量必须大于0");
            }
        }

        warehouseEntryService.createEntry(entry);
    }
}
