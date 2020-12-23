package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.config.exception.GlobalException;
import org.jc.backend.entity.VO.PurchaseOrderEntryWithProductsVO;
import org.jc.backend.service.PurchaseOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Api(tags = "PurchaseOrder Related")
@RestController
@RequestMapping("/purchaseOrder")
public class PurchaseOrderController {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderController.class);

    public final PurchaseOrderService purchaseOrderService;

    private PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = Boolean.class)
    @PutMapping("/createNewPurchaseOrder")
    public void createNewPurchaseOrder(@RequestBody @Validated PurchaseOrderEntryWithProductsVO purchaseOrderEntry) throws GlobalException {
        logger.info("PUT Request to /createNewPurchaseOrder");

        purchaseOrderService.createNewPurchaseOrder(purchaseOrderEntry);
    }

    @ApiOperation(value = "", response = PurchaseOrderEntryWithProductsVO.class,
        notes = "companyID can be null, if null, query all")
    @GetMapping("/getPurchaseOrdersWithinDateRangeByCompanyID")
    public List<PurchaseOrderEntryWithProductsVO> getPurchaseOrdersWithinDateRangeByCompanyID(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam(value = "id", defaultValue = "-1") Integer id) throws GlobalException {
        logger.info("GET Request to /getPurchaseOrdersWithinDateRangeByCompanyID, start date: " + startDateString +
                ", end date： " + endDateString + ", id: " + id);

        // parse Date to verify passed param
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate, endDate;
        try {
            startDate = dateFormat.parse(startDateString);
            endDate = dateFormat.parse(endDateString);
        } catch (ParseException e) {
            String errorInfo = "Invalid date range: " + startDateString + " -- " + endDateString;
            logger.info(errorInfo);
            throw new GlobalException(errorInfo);
        }

        return purchaseOrderService.getOrdersWithinDateRangeByCompanyID(startDate, endDate, id);
    }
}
