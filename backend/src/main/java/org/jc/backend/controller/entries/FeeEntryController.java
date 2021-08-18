package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.entity.FeeCategoryO;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.jc.backend.service.FeeEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Indexed
@Api(tags = "Fees Entry Related")
@RestController
@RequestMapping("/feeEntry")
public class FeeEntryController {
    private static final Logger logger = LoggerFactory.getLogger(FeeEntryController.class);

    private final FeeEntryService feeEntryService;

    public FeeEntryController(FeeEntryService feeEntryService) {
        this.feeEntryService = feeEntryService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = FeeCategoryO.class)
    @GetMapping("/getFeeCategories")
    public List<FeeCategoryO> getFeeCategories() {
        logger.info("/feeEntry/getFeeCategories");

        return feeEntryService.getFeeCategories();
    }

    @ApiOperation(value = "", response = void.class)
    @PostMapping("/updateFeeCategories")
    public void updateFeeCategories(
            @RequestBody @Validated ListUpdateVO<FeeCategoryO> updateVO) {
        logger.info("/feeEntry/updateFeeCategories");

        feeEntryService.updateFeeCategories(updateVO.getElements());
    }

    @ApiOperation(value = "", response = void.class)
    @PutMapping("/createEntry")
    public void createEntry() {

    }
}
