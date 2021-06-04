package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jc.backend.entity.FactoryBrandO;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.jc.backend.service.FactoryBrandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Indexed
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

    @ApiOperation(value = "", response = void.class)
    @RequiresPermissions("system:factoryBrands")
    @PostMapping("/updateAllFactoryBrands")
    public void updateAllBrands(@RequestBody @Validated ListUpdateVO<FactoryBrandO> updateVO) {
        logger.info("Post Request to /factoryBrand/updateAllFactoryBrands, info {}", updateVO);

        factoryBrandService.updateAllBrands(updateVO);
    }
}
