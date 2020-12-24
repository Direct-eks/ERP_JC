package org.jc.backend.controller;

import io.swagger.annotations.Api;
import org.jc.backend.service.QuotaEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "QuotaEntry Related")
@RestController
@RequestMapping("/quotaEntry")
public class QuotaEntryController {
    private static final Logger logger = LoggerFactory.getLogger(QuotaEntryController.class);

    private final QuotaEntryService quotaEntryService;

    public QuotaEntryController(QuotaEntryService quotaEntryService) {
        this.quotaEntryService = quotaEntryService;
    }

    /* ------------------------------ API ------------------------------ */


}
