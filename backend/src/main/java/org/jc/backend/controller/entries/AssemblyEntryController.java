package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import org.jc.backend.service.WarehouseEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Indexed
@Api(tags = "AssemblyEntry Related")
@RestController
@RequestMapping("/assemblyEntry")
public class AssemblyEntryController {
    private static final Logger logger = LoggerFactory.getLogger(AssemblyEntryController.class);

    private final WarehouseEntryService assemblyEntryService;

    public AssemblyEntryController(WarehouseEntryService assemblyEntryService) {
        this.assemblyEntryService = assemblyEntryService;
    }

    /* ------------------------------ API ------------------------------ */


}
