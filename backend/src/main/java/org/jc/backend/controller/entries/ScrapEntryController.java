package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import org.jc.backend.service.WarehouseEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Indexed
@Api(tags = "ScrapEntry Related")
@RestController
@RequestMapping("/scrapEntry")
public class ScrapEntryController {
    private static final Logger logger = LoggerFactory.getLogger(ScrapEntryController.class);

    private final WarehouseEntryService scrapEntryService;

    public ScrapEntryController(WarehouseEntryService scrapEntryService) {
        this.scrapEntryService = scrapEntryService;
    }

    /* ------------------------------ API ------------------------------ */


}
