package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.VO.QuoteEntryWithProductsVO;
import org.jc.backend.service.QuoteEntryService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Indexed
@Api(tags = "QuoteEntry Related")
@RestController
@RequestMapping("/quoteEntry")
public class QuoteEntryController {
    private static final Logger logger = LoggerFactory.getLogger(QuoteEntryController.class);

    private final QuoteEntryService quoteEntryService;

    public QuoteEntryController(QuoteEntryService quoteEntryService) {
        this.quoteEntryService = quoteEntryService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = void.class)
    @RequiresPermissions("quota:Creation")
    @PutMapping("/createOrder")
    public void createQuota(@RequestBody @Validated QuoteEntryWithProductsVO quoteEntryWithProductsVO) {
        logger.info("PUT Request to /quotaEntry/createOrder, data: {}", quoteEntryWithProductsVO.toString());

        quoteEntryService.createOrder(quoteEntryWithProductsVO);
    }

    @ApiOperation(value = "", response = QuoteEntryWithProductsVO.class,
            notes = "companyID can be null, if null, query all")
    @RequiresPermissions("quota:Query")
    @GetMapping("/getOrdersInDateRangeByCompanyID")
    public List<QuoteEntryWithProductsVO> getQuotasInDateRangeByCompanyID(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam(value = "companyID", defaultValue = "-1") int companyID) throws GlobalParamException {
        logger.info("GET Request to /quotaEntry/getOrdersInDateRangeByCompanyID, start date: {}; " +
                "end date: {}; id: {}", startDateString, endDateString, companyID);

        Date startDate = MyUtils.parseAndCheckDateString(startDateString);
        Date endDate = MyUtils.parseAndCheckDateString(endDateString);

        return quoteEntryService.getOrdersInDateRangeByCompanyID(startDate, endDate, companyID);
    }

    @ApiOperation(value = "", response = QuoteEntryWithProductsVO.class)
    @RequiresPermissions("quota:Query")
    @GetMapping("/getOrdersByCompanyID/{id}")
    public List<QuoteEntryWithProductsVO> getQuotasByCompanyID(@PathVariable("id") int id) {
        logger.info("GET Request to /quotaEntry/getOrdersByCompanyID, id: {}", id);

        return quoteEntryService.getOrdersByCompanyID(id);
    }

    @ApiOperation(value = "", response = void.class)
    @RequiresPermissions("quota:Modification")
    @PatchMapping("/modifyOrder")
    public void modifyQuota(@RequestBody @Validated QuoteEntryWithProductsVO modifyQuotaVO) {
        logger.info("PATCH Request to /quotaEntry/modifyOrder, data: {}", modifyQuotaVO.toString());

        quoteEntryService.modifyOrder(modifyQuotaVO);
    }

    @ApiOperation(value = "", response = void.class)
    @DeleteMapping("/deleteOrder/{id}")
    public void deleteQuota(@PathVariable("id") String id) {
        logger.info("DELETE Request to /quotaEntry/deleteOrder, id: {}", id);

        quoteEntryService.deleteOrder(id);
    }

}
