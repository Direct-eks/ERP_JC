package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.entity.MeasurementUnitO;
import org.jc.backend.service.MeasurementUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "MeasurementUnit Related")
@RestController
@RequestMapping("/unit")
public class MeasurementUnitController {

    private static final Logger logger = LoggerFactory.getLogger(MeasurementUnitController.class);

    private MeasurementUnitService measurementUnitService;

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

}
