package org.jc.backend.controller;

import io.swagger.annotations.Api;
import org.jc.backend.service.ShippingCostEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "ShippingCostEntry Related")
@RestController
@RequestMapping("/shippingCostEntry")
public class ShippingCostEntryController {
    private static final Logger logger = LoggerFactory.getLogger(ShippingCostEntryController.class);

    private final ShippingCostEntryService shippingCostEntryService;

    public ShippingCostEntryController(ShippingCostEntryService shippingCostEntryService) {
        this.shippingCostEntryService = shippingCostEntryService;
    }

    /* ------------------------------ API ------------------------------ */
}
