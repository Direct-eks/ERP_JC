package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.entity.SkuFullO;
import org.jc.backend.service.SkuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "Sku Related")
@RestController
@RequestMapping("/sku")
public class SkuController {

    private static final Logger logger = LoggerFactory.getLogger(SkuController.class);

    private final SkuService skuService;

    public SkuController(SkuService skuService) {
        this.skuService = skuService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = SkuFullO.class)
    @GetMapping("/getFullSkuByModel/{id}")
    public List<SkuFullO> getFullSkuByModel(@PathVariable("id") int id) {
        logger.info("GET Request to /sku/getFullSkuByModel, id: " + id);

        return skuService.getFullSkuByModel(id);
    }
}
