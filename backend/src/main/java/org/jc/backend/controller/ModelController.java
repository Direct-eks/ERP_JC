package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.ModelCategoryO;
import org.jc.backend.entity.ModelO;
import org.jc.backend.service.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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
    
}
