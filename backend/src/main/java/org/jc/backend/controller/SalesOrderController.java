package org.jc.backend.controller;

import io.swagger.annotations.Api;
import org.jc.backend.service.SalesOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "SalesOrder Related")
@RestController
@RequestMapping("/salesOrder")
public class SalesOrderController {
    private static final Logger logger = LoggerFactory.getLogger(SalesOrderController.class);

    private final SalesOrderService salesOrderService;

    public SalesOrderController(SalesOrderService salesOrderService) {
        this.salesOrderService = salesOrderService;
    }

    /* ------------------------------ API ------------------------------ */


}
