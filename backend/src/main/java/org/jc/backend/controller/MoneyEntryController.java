package org.jc.backend.controller;

import io.swagger.annotations.Api;
import org.jc.backend.service.MoneyEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "MoneyEntry Related")
@RestController
@RequestMapping("/moneyEntry")
public class MoneyEntryController {
    private static final Logger logger = LoggerFactory.getLogger(MoneyEntryController.class);

    private final MoneyEntryService moneyEntryService;

    public MoneyEntryController(MoneyEntryService moneyEntryService) {
        this.moneyEntryService = moneyEntryService;
    }

    /* ------------------------------ API ------------------------------ */
}
