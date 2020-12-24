package org.jc.backend.controller;

import io.swagger.annotations.Api;
import org.jc.backend.service.OutboundEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "OutboundEntry Related")
@RestController
@RequestMapping("/outboundEntry")
public class OutboundEntryController {
    private static final Logger logger = LoggerFactory.getLogger(OutboundEntryController.class);

    private final OutboundEntryService outboundEntryService;

    public OutboundEntryController(OutboundEntryService outboundEntryService) {
        this.outboundEntryService = outboundEntryService;
    }

    /* ------------------------------ API ------------------------------ */


}
