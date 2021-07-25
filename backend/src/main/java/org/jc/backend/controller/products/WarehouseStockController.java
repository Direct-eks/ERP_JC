package org.jc.backend.controller.products;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.entity.StatO.EntryProductVO;
import org.jc.backend.entity.StatO.StockStatO;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.jc.backend.entity.WarehouseStockO;
import org.jc.backend.service.WarehouseStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Indexed
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

    @ApiOperation(value = "", response = WarehouseStockO.class)
    @GetMapping("/getProductsByWarehouseStockID/{id}")
    public List<EntryProductVO> getProductsByWarehouseStockID(@PathVariable("id") int id) {
        logger.info("GET Request to /warehouseStock/getProductsByWarehouseStockID, id: " + id);

        return warehouseStockService.getProductsByWarehouseStockID(id);
    }

    @ApiOperation(value = "", response = void.class)
    @PostMapping("/updateWarehouseStockInitialInfo")
    public void updateWarehouseStockInitialInfo(@RequestBody @Validated ListUpdateVO<WarehouseStockO> updateVO) {
        logger.info("POST Request to /warehouseStock/updateWarehouseStockInitialInfo");

        warehouseStockService.updateWarehouseStockInitialInfo(updateVO.getElements());
    }

    @ApiOperation(value = "", response = StockStatO.class)
    @GetMapping("/getWarehouseStockReport")
    public List<StockStatO> getWarehouseStockReport(
            @RequestParam("categoryID") int categoryID,
            @RequestParam("warehouseID") int warehouseID,
            @RequestParam("factoryBrand") String factoryBrand,
            @RequestParam("code") String code
    ) {
        logger.info("GET Request to /warehouseStock/getWarehouseStockReport, " +
                "categoryID: {}, warehouseID: {}, factoryBrand: {}, code {}",
                categoryID, warehouseID, factoryBrand, code);

        return warehouseStockService.getWarehouseStockReport(categoryID, warehouseID,
                factoryBrand, code);
    }

    @ApiOperation(value = "", response = void.class)
    @GetMapping("/exportStockReport")
    public void exportStockReport(HttpServletResponse response) {
        logger.info("GET Request to /warehouseStock/exportStockReport");

        warehouseStockService.exportStockReport(response);
    }
}
