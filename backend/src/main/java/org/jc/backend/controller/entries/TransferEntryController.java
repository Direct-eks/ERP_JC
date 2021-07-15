package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import org.jc.backend.service.WarehouseEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Indexed
@Api(tags = "TransferEntry Related")
@RestController
@RequestMapping("/transferEntry")
public class TransferEntryController {
    private static final Logger logger = LoggerFactory.getLogger(TransferEntryController.class);

    private final WarehouseEntryService transferEntryService;

    public TransferEntryController(WarehouseEntryService transferEntryService) {
        this.transferEntryService = transferEntryService;
    }

    /* ------------------------------ API ------------------------------ */


}
