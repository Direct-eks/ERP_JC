package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import org.jc.backend.service.WarehouseEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Indexed
@Api(tags = "InventoryLossEntry Related")
@RestController
@RequestMapping("/inventoryLossEntry")
public class InventoryLossEntryController {
    private static final Logger logger = LoggerFactory.getLogger(InventoryLossEntryController.class);

    private final WarehouseEntryService inventoryLossEntryService;

    public InventoryLossEntryController(WarehouseEntryService inventoryLossEntryService) {
        this.inventoryLossEntryService = inventoryLossEntryService;
    }

    /* ------------------------------ API ------------------------------ */

}
