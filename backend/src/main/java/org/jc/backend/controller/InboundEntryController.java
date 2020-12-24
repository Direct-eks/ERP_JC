package org.jc.backend.controller;

import io.swagger.annotations.Api;
import org.jc.backend.service.InboundEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "InboundEntry Related")
@RestController
@RequestMapping("/inboundEntry")
public class InboundEntryController {
    private static final Logger logger = LoggerFactory.getLogger(InboundEntryController.class);

    private final InboundEntryService inboundEntryService;

    public InboundEntryController(InboundEntryService inboundEntryService) {
        this.inboundEntryService = inboundEntryService;
    }

    /* ------------------------------ API ------------------------------ */


}
