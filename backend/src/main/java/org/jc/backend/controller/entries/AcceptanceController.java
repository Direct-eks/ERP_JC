package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.entity.AcceptanceEntryO;
import org.jc.backend.service.AcceptanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Indexed
@Api(tags = "Acceptance Entry Related")
@RestController
@RequestMapping("/acceptanceEntry")
public class AcceptanceController {

    private static final Logger logger = LoggerFactory.getLogger(AcceptanceController.class);

    private final AcceptanceService acceptanceService;

    public AcceptanceController(AcceptanceService acceptanceService) {
        this.acceptanceService = acceptanceService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = void.class)
    @PutMapping("/createEntry")
    public void createEntry(@RequestBody @Validated AcceptanceEntryO entryO) {
        logger.info("PUT Request to /acceptanceEntry/createEntry, data: {}", entryO);

        acceptanceService.createEntry(entryO);
    }

    @ApiOperation(value = "", response = AcceptanceEntryO.class)
    @GetMapping("/getEntries")
    public List<AcceptanceEntryO> getEntries() {
        return null;
    }
}
