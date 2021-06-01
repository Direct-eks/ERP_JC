package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.ModelCategoryO;
import org.jc.backend.entity.ModelO;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.jc.backend.service.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "Model Related")
@RestController
@RequestMapping("/model")
public class ModelController {

    private static final Logger logger = LoggerFactory.getLogger(ModelController.class);

    private final ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = ModelCategoryO.class)
    @GetMapping("/getModelCategories")
    public List<ModelCategoryO> getModelCategories() {
        logger.info("GET Request to /model/getModelCategories");

        return modelService.getModelCategories();
    }

    @ApiOperation(value = "", response = ModelO.class)
    @GetMapping("/getModelsByCategory/{id}")
    public List<ModelO> getModelsByCategory(@PathVariable("id") int id) {
        logger.info("GET Request to /model/getModelsByCategory, id: " + id);

        return modelService.getModelsByCategory(id);
    }

    @ApiOperation(value = "", response = ModelO.class)
    @GetMapping("getModelsByName")
    public List<ModelO> getModelsByName(
            @RequestParam("name") String name,
            @RequestParam("method") String method
    ) throws GlobalParamException {
        logger.info("GET Request to /model/getModelsByName, name: " + name +
                ", method: " + method);

        switch (method) {
            case "prefix":
            case "infix":
            case "suffix":
                break;
            default:
                throw new GlobalParamException("invalid search method param");
        }

        return modelService.getModelsByName(name, method);
    }

    @ApiOperation(value = "", response = void.class)
    @PostMapping("/updateModelsWithCategory")
    public void updateModelsWithCategory(
            @RequestParam("categoryID") int categoryID,
            @RequestParam("brands") String brands,
            @RequestBody @Validated ListUpdateVO<ModelO> updateVO
    ) throws GlobalParamException {
        logger.info("POST Request to /model/updateModelsWithCategory, category: {}, " +
                "brands: {}, info: {}", categoryID, brands, updateVO);

        if (categoryID == -1) throw new GlobalParamException("categoryID error");

        int[] brandIDs;
        if (brands.equals("")) {
            brandIDs = new int[0];
        }
        else {
            String[] brandsArray = brands.split(",");
            brandIDs = new int[brandsArray.length];
            for (int i = 0; i < brandsArray.length; ++i) {
                try {
                    brandIDs[i] = Integer.parseInt(brandsArray[i]);
                } catch (NumberFormatException e) {
                    throw new GlobalParamException("brandID format error");
                }
            }
        }

        modelService.updateModelsWithCategory(categoryID, brandIDs, updateVO);
    }

    @ApiOperation(value = "", response = void.class)
    @GetMapping("/exportAllModels")
    public void exportAllModels(HttpServletResponse response) {
        logger.info("GET Request to /model/exportAllModels");

        modelService.exportAllModels(response);
    }
}
