package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.entity.WarehouseStockO;
import org.jc.backend.service.WarehouseStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "WarehouseStock Related")
@RestController
@RequestMapping("/warehouseStock")
public class WarehouseStockController {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseStockController.class);

    private final WarehouseStockService warehouseStockService;

    public WarehouseStockController(WarehouseStockService warehouseStockService) {
        this.warehouseStockService = warehouseStockService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = WarehouseStockO.class)
    @GetMapping("/getWarehouseStocksBySku/{id}")
    public List<WarehouseStockO> getWarehouseStocksBySku(@PathVariable("id") int id) {
        logger.info("GET Request to /warehouseStock/getWarehouseStockBySku, id: " + id);

        return warehouseStockService.getWarehouseStocksBySku(id);
    }
}
