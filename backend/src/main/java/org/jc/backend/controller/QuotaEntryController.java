package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.config.exception.GlobalException;
import org.jc.backend.entity.VO.QuotaEntryWithProductsVO;
import org.jc.backend.service.QuotaEntryService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

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

    @ApiOperation(value = "", response = void.class)
    @PutMapping("/createOrder")
    public void createQuota(@RequestBody @Validated QuotaEntryWithProductsVO quotaEntryWithProductsVO) {
        logger.info("PUT Request to /quotaEntry/createOrder");

        quotaEntryService.createOrder(quotaEntryWithProductsVO);
    }

    @ApiOperation(value = "", response = QuotaEntryWithProductsVO.class,
            notes = "companyID can be null, if null, query all")
    @GetMapping("/getOrdersInDateRangeByCompanyID")
    public List<QuotaEntryWithProductsVO> getQuotasInDateRangeByCompanyID(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam(value = "companyID", defaultValue = "-1") int companyID) throws GlobalException {
        logger.info("GET Request to /quotaEntry/getOrdersInDateRangeByCompanyID, start date: " +
                startDateString + ", end date： " + endDateString + ", id: " + companyID);

        Date startDate = MyUtils.parseAndCheckDateString(startDateString);
        Date endDate = MyUtils.parseAndCheckDateString(endDateString);

        return quotaEntryService.getOrdersInDateRangeByCompanyID(startDate, endDate, companyID);
    }

    @ApiOperation(value = "", response = QuotaEntryWithProductsVO.class)
    @GetMapping("/getOrdersByCompanyID/{id}")
    public List<QuotaEntryWithProductsVO> getQuotasByCompanyID(@PathVariable("id") int id) {
        logger.info("GET Request to /quotaEntry/getOrdersByCompanyID, id: " + id);

        return quotaEntryService.getOrdersByCompanyID(id);
    }

    @ApiOperation(value = "", response = void.class)
    @PatchMapping("/modifyOrder")
    public void modifyQuota(@RequestBody @Validated QuotaEntryWithProductsVO quotaEntryWithProductsVO) {
        logger.info("PATCH Request to /quotaEntry/modifyOrder");

        quotaEntryService.modifyOrder(quotaEntryWithProductsVO);
    }

    @ApiOperation(value = "", response = void.class)
    @DeleteMapping("/deleteOrder/{id}")
    public void deleteQuota(@PathVariable("id") String id) {
        logger.info("DELETE Request to /quotaEntry/deleteOrder, id: " + id);

        quotaEntryService.deleteOrder(id);
    }

}
