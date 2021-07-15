package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import org.jc.backend.service.WarehouseEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Indexed
@Api(tags = "MaterialApplyEntry Related")
@RestController
@RequestMapping("/materialApplyEntry")
public class MaterialApplyEntryController {
    private static final Logger logger = LoggerFactory.getLogger(MaterialApplyEntryController.class);

    private final WarehouseEntryService materialApplyEntryService;

    public MaterialApplyEntryController(WarehouseEntryService materialApplyEntryService) {
        this.materialApplyEntryService = materialApplyEntryService;
    }

    /* ------------------------------ API ------------------------------ */


}
