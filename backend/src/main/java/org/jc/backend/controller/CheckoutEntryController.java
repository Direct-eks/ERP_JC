package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.entity.VO.CheckoutEntryWithProductsVO;
import org.jc.backend.service.CheckoutEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "CheckoutEntry Related")
@RestController
@RequestMapping("/checkoutEntry")
public class CheckoutEntryController {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutEntryController.class);

    private final CheckoutEntryService checkoutEntryService;

    public CheckoutEntryController(CheckoutEntryService checkoutEntryService) {
        this.checkoutEntryService = checkoutEntryService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = void.class)
    @PutMapping("/createEntry")
    public void createEntry(@RequestBody @Validated CheckoutEntryWithProductsVO checkoutEntryWithProductsVO) {
        logger.info("PUT Request to /checkoutEntry/createEntry");

        checkoutEntryService.createEntry(checkoutEntryWithProductsVO);
    }
}
