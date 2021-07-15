package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import org.jc.backend.service.WarehouseEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Indexed
@Api(tags = "InventoryProfitEntry Related")
@RestController
@RequestMapping("/inventoryProfitEntry")
public class InventoryProfitEntryController {
    private static final Logger logger = LoggerFactory.getLogger(InventoryProfitEntryController.class);

    private final WarehouseEntryService inventoryProfitEntryService;

    public InventoryProfitEntryController(WarehouseEntryService inventoryProfitEntryService) {
        this.inventoryProfitEntryService = inventoryProfitEntryService;
    }

    /* ------------------------------ API ------------------------------ */

}
