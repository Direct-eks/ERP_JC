package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.entity.FactoryBrandO;
import org.jc.backend.service.FactoryBrandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "FactoryBrand Related")
@RestController
@RequestMapping("/factoryBrand")
public class FactoryBrandController {

    private static final Logger logger = LoggerFactory.getLogger(FactoryBrandController.class);

    private final FactoryBrandService factoryBrandService;

    public FactoryBrandController(FactoryBrandService factoryBrandService) {
        this.factoryBrandService = factoryBrandService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = FactoryBrandO.class)
    @GetMapping("/allFactoryBrands")
    public List<FactoryBrandO> getAllFactoryBrands() {
        logger.info("GET Request to /factoryBrand/allFactoryBrands");

        return factoryBrandService.getAllFactoryBrands();
    }
}
