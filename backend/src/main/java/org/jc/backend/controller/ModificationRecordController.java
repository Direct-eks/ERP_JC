package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.ModificationO;
import org.jc.backend.service.ModificationRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Indexed
@Api(tags = "ModificationRecord Related")
@RestController
@RequestMapping("/modificationRecord")
public class ModificationRecordController {

    private static final Logger logger = LoggerFactory.getLogger(ModificationRecordController.class);

    private final ModificationRecordService modificationRecordService;

    public ModificationRecordController(ModificationRecordService modificationRecordService) {
        this.modificationRecordService = modificationRecordService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = ModificationO.class)
    @GetMapping("/getRecordsBySerial/{serial}")
    public List<ModificationO> getRecordsBySerial(@PathVariable("serial") String serial) {
        logger.info("GET Request to /modificationRecord/getRecordsBySerial, serial: " + serial);

        return modificationRecordService.getEntryRecordsBySerial(serial);
    }

    @ApiOperation(value = "", response = ModificationO.class)
    @GetMapping("/getRecordsByCategoryAndID")
    public List<ModificationO> getRecordsBySerial(
            @RequestParam("category") String category,
            @RequestParam("id") int id
    ) throws GlobalParamException {
        logger.info("GET Request to /modificationRecord/getRecordsByCategoryAndID," +
                "category: {}, id: {}", category, id);

        if (category.isBlank() || id <= 0) {
            throw new GlobalParamException("param value error");
        }

        return modificationRecordService.getMiscRecordsByCategoryAndID(category, id);
    }
}
