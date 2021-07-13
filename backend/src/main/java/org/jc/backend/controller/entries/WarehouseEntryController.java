package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.service.WarehouseEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value = "", response = void.class)
    @PutMapping("/createEntry")
    public void createEntry() {

    }
}
