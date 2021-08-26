package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.MiscellaneousDataO;
import org.jc.backend.service.MiscellaneousDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Indexed
@Api(tags = "MiscellaneousData Related")
@RestController
@RequestMapping("/miscellaneousData")
public class MiscellaneousDataController {
    private static final Logger logger = LoggerFactory.getLogger(MiscellaneousDataController.class);

    private final MiscellaneousDataService miscellaneousDataService;

    public MiscellaneousDataController(MiscellaneousDataService miscellaneousDataService) {
        this.miscellaneousDataService = miscellaneousDataService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = String.class)
    @RequiresAuthentication
    @GetMapping("/getAllTaxRateOptions")
    public List<String> getAllTaxRateOptions() {
        logger.info("GET Request to /miscellaneousData/getAllTaxRateOptions");

        return miscellaneousDataService.getAllTaxRateOptions();
    }

    @ApiOperation(value = "", response = String.class)
    @RequiresAuthentication
    @GetMapping("/getLastBackupTime")
    public String getLastBackupTime() {
        logger.info("GET Request to /miscellaneousData/getLastBackupTime");

        return miscellaneousDataService.getLastBackupTime();
    }

    @ApiOperation(value = "", response = Integer.class)
    @GetMapping("/getAutoBackupStatus")
    public Integer getAutoBackupStatus() {
        logger.info("GET Request to /miscellaneousData/getAutoBackupStatus");

        return miscellaneousDataService.isAutoBackupEnabled() ? 1 : 0;
    }

    @ApiOperation(value = "", response = void.class)
    @PostMapping("/updateAutoBackupStatus")
    public void updateAutoBackupStatus(@RequestParam("status") int status) {
        logger.info("POST Request to /miscellaneousData/updateAutoBackupStatus: {}", status);

        miscellaneousDataService.updateAutoBackupStatus(status);
    }

    @ApiOperation(value = "", response = void.class)
    @RequiresAuthentication
    @PostMapping("/backupDatabase")
    public void backupDatabase() throws GlobalParamException {
        logger.info("POST Request to /miscellaneousData/backupDatabase");

        miscellaneousDataService.backupDatabase();
    }

    @ApiOperation(value = "", response = String.class)
    @RequiresAuthentication
    @GetMapping("/getAuditMonths")
    public List<MiscellaneousDataO> getAuditMonths() {
        logger.info("GET Request to /miscellaneousData/getAuditMonths");

        return miscellaneousDataService.queryAuditMonths();
    }
}
