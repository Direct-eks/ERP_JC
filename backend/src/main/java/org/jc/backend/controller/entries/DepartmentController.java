package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jc.backend.entity.DepartmentO;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.jc.backend.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Indexed
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

    @ApiOperation(value = "", response = void.class)
    @RequiresPermissions("system:departments")
    @PostMapping("/updateDepartments")
    public void updateDepartments(@RequestBody @Validated ListUpdateVO<DepartmentO> updateVO) {
        logger.info("POST Request to /department/updateDepartments, info: {}", updateVO);

        departmentService.updateDepartments(updateVO);
    }
}