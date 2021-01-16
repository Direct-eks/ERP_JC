package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.entity.DepartmentO;
import org.jc.backend.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Department Related")
@RestController
@RequestMapping("/department")
public class DepartmentController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = DepartmentO.class)
    @GetMapping("/getDepartmentOptions")
    public List<DepartmentO> getDepartmentOptions() {
        logger.info("GET Request to /department/getDepartmentOptions");

        return departmentService.getDepartmentOptions();
    }
}