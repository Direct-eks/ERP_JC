package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.entity.MeasurementUnitO;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.jc.backend.service.MeasurementUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "MeasurementUnit Related")
@RestController
@RequestMapping("/unit")
public class MeasurementUnitController {

    private static final Logger logger = LoggerFactory.getLogger(MeasurementUnitController.class);

    private final MeasurementUnitService measurementUnitService;

    public MeasurementUnitController(MeasurementUnitService measurementUnitService) {
        this.measurementUnitService = measurementUnitService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = MeasurementUnitO.class)
    @GetMapping("/getAllUnits")
    public List<MeasurementUnitO> getAllUnits() {
        logger.info("GET Request to /unit/getAllUnits");

        return measurementUnitService.getAllUnits();
    }

    @ApiOperation(value = "", response = void.class)
    @PostMapping("/updateUnits")
    public void updateUnits(@RequestBody @Validated ListUpdateVO<MeasurementUnitO> updateVO) {
        logger.info("PATCH Request to /unit/updateUnits, info {}", updateVO);

        measurementUnitService.updateUnits(updateVO);
    }

}
