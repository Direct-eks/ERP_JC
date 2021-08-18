package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.entity.FeeCategoryO;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.jc.backend.service.FeesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Indexed
@Api(tags = "Fees Related")
@RestController
@RequestMapping("/fees")
public class FeesController {
    private static final Logger logger = LoggerFactory.getLogger(FeesController.class);

    private final FeesService feesService;

    public FeesController(FeesService feesService) {
        this.feesService = feesService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = FeeCategoryO.class)
    @GetMapping("/getFeeCategories")
    public List<FeeCategoryO> getFeeCategories() {
        logger.info("/fees/getFeeCategories");

        return feesService.getFeeCategories();
    }

    @ApiOperation(value = "", response = void.class)
    @PostMapping("/updateFeeCategories")
    public void updateFeeCategories(
            @RequestBody @Validated ListUpdateVO<FeeCategoryO> updateVO) {
        logger.info("/fees/updateFeeCategories");

        feesService.updateFeeCategories(updateVO.getElements());
    }

    @ApiOperation(value = "", response = void.class)
    @PutMapping("/createEntry")
    public void createEntry() {

    }
}
