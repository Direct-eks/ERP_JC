package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.SkuFullO;
import org.jc.backend.entity.SkuO;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.jc.backend.service.SkuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Indexed
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

    @ApiOperation(value = "", response = SkuFullO.class)
    @GetMapping("/getSkusByCategoryAndFactoryBrand")
    public List<SkuFullO> getSkusByCategoryAndFactoryBrand(
            @RequestParam("modelCategoryID") int modelCategoryID,
            @RequestParam("factoryBrandID") int factoryBrandID
    ) {
        logger.info("GET Request to /sku/getSkusByModelCategoryAndFactoryBrand, modelCategoryID: " +
                modelCategoryID + ", factoryBrandID: " + factoryBrandID);

        return skuService.getSkusByCategoryAndFactoryBrand(modelCategoryID, factoryBrandID);
    }

    @ApiOperation(value = "", response = void.class)
    @PostMapping("/modifySkuPricing")
    public void modifySkuPricing(@Validated @RequestBody ListUpdateVO<SkuFullO> listUpdateVO) {
        logger.info("POST Request to /sku/modifySkuPricing, data: {}", listUpdateVO.getElements().toString());

        skuService.modifySkuPricing(listUpdateVO.getElements());
    }

    @ApiOperation(value = "", response = void.class)
    @RequiresPermissions("system:sku")
    @PostMapping("/updateSku")
    public void updateSku(
            @RequestParam("modelID") int modelID,
            @Validated @RequestBody ListUpdateVO<SkuO> updateVO
    ) {
        logger.info("POST Request to /sku/updateSku, modelID: {}, info {}", modelID, updateVO);

        skuService.updateSku(modelID, updateVO);
    }

    @ApiOperation(value = "", response = void.class)
    @RequiresPermissions("system:sku")
    @PostMapping("/updateSkuBulk")
    public void updateSkuBulk(
            @RequestParam("modelIDs") String modelIDs,
            @Validated @RequestBody ListUpdateVO<SkuO> updateVO
    ) throws GlobalParamException {
        logger.info("POST Request to /sku/updateSkuBulk, ");

        if (modelIDs.equals("")) {
            throw new GlobalParamException("modelIDs blank error");
        }
        String[] modelIDsString = modelIDs.split(",");
        int[] modelIDsArray = new int[modelIDsString.length];
        for (int i = 0; i < modelIDsString.length; ++i) {
            try {
                modelIDsArray[i] = Integer.parseInt(modelIDsString[i]);
            } catch (NumberFormatException e) {
                throw new GlobalParamException("modelID format error");
            }
        }

        int[] brandIDs = new int[updateVO.getElements().size()];
        int i = 0;
        for (var e : updateVO.getElements()) {
            brandIDs[i++] = e.getFactoryBrandID();
        }

        skuService.updateSkuBulk(modelIDsArray, brandIDs);
    }
}
