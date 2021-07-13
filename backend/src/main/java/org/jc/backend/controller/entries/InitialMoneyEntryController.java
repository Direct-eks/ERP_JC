package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import org.jc.backend.service.InitialMoneyEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Indexed
@Api(tags = "InitialMoneyEntry Related")
@RestController
@RequestMapping("/initialMoneyEntry")
public class InitialMoneyEntryController {
    private static final Logger logger = LoggerFactory.getLogger(InitialMoneyEntryController.class);

    private final InitialMoneyEntryService initialMoneyEntryService;

    public InitialMoneyEntryController(InitialMoneyEntryService initialMoneyEntryService) {
        this.initialMoneyEntryService = initialMoneyEntryService;
    }

    /* ------------------------------ API ------------------------------ */
}
