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
    @RequiresPermissions("quote:Creation")
    @PutMapping("/createQuote")
    public void createQuote(@RequestBody @Validated QuoteEntryWithProductsVO quoteEntryWithProductsVO) {
        logger.info("PUT Request to /quoteEntry/createQuote, data: {}", quoteEntryWithProductsVO.toString());

        quoteEntryService.createQuote(quoteEntryWithProductsVO);
    }

    @ApiOperation(value = "", response = QuoteEntryWithProductsVO.class,
            notes = "companyID can be null, if null, query all")
    @RequiresPermissions("quote:Query")
    @GetMapping("/getQuotesInDateRangeByCompanyID")
    public List<QuoteEntryWithProductsVO> getQuotesInDateRangeByCompanyID(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam(value = "companyID", defaultValue = "-1") int companyID) throws GlobalParamException {
        logger.info("GET Request to /quoteEntry/getQuotesInDateRangeByCompanyID, start date: {}; " +
                "end date: {}; id: {}", startDateString, endDateString, companyID);

        Date startDate = MyUtils.parseAndCheckDateString(startDateString);
        Date endDate = MyUtils.parseAndCheckDateString(endDateString);

        return quoteEntryService.getQuotesInDateRangeByCompanyID(startDate, endDate, companyID);
    }

    @ApiOperation(value = "", response = QuoteEntryWithProductsVO.class)
    @RequiresPermissions("quote:Query")
    @GetMapping("/getQuotesByCompanyID/{id}")
    public List<QuoteEntryWithProductsVO> getQuotesByCompanyID(@PathVariable("id") int id) {
        logger.info("GET Request to /quoteEntry/getQuotesByCompanyID, id: {}", id);

        return quoteEntryService.getQuotesByCompanyID(id);
    }

    @ApiOperation(value = "", response = void.class)
    @RequiresPermissions("quote:Modification")
    @PatchMapping("/modifyQuote")
    public void modifyQuote(@RequestBody @Validated QuoteEntryWithProductsVO modifyQuotaVO) {
        logger.info("PATCH Request to /quoteEntry/modifyQuote, data: {}", modifyQuotaVO.toString());

        quoteEntryService.modifyQuote(modifyQuotaVO);
    }

    @ApiOperation(value = "", response = void.class)
    @DeleteMapping("/deleteQuote/{id}")
    public void deleteQuote(@PathVariable("id") String id) {
        logger.info("DELETE Request to /quoteEntry/deleteQuote, id: {}", id);

        quoteEntryService.deleteQuote(id);
    }

}
