package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.entity.WarehouseO;
import org.jc.backend.service.WarehouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Indexed
@Api(tags = "Warehouse Related")
@RestController
@RequestMapping("/warehouse")
public class WarehouseController {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseController.class);

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = WarehouseO.class)
    @GetMapping("/getWarehouseOptions")
    public List<WarehouseO> getWarehouseOptions() {
        logger.info("GET Request to /warehouse/getWarehouseOptions");

        return warehouseService.getWarehouseOptions();
    }
}
