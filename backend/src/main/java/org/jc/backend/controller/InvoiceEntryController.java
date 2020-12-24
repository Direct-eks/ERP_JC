package org.jc.backend.controller;

import io.swagger.annotations.Api;
import org.jc.backend.service.InvoiceEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "InvoiceEntry Related")
@RestController
@RequestMapping("/invoiceEntry")
public class InvoiceEntryController {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceEntryController.class);

    private final InvoiceEntryService invoiceEntryService;

    public InvoiceEntryController(InvoiceEntryService invoiceEntryService) {
        this.invoiceEntryService = invoiceEntryService;
    }

    /* ------------------------------ API ------------------------------ */
}
