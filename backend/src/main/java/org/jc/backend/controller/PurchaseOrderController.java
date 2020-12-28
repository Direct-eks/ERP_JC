package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.config.exception.GlobalException;
import org.jc.backend.entity.VO.PurchaseOrderEntryWithProductsVO;
import org.jc.backend.entity.VO.PurchaseOrderModifyVO;
import org.jc.backend.service.PurchaseOrderService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "", response = void.class)
    @PutMapping("/createOrder")
    public void createOrder(@RequestBody @Validated PurchaseOrderEntryWithProductsVO purchaseOrderEntry) {
        logger.info("PUT Request to /purchaseOrder/createOrder");

        purchaseOrderService.createOrder(purchaseOrderEntry);
    }

    @ApiOperation(value = "", response = PurchaseOrderEntryWithProductsVO.class,
        notes = "companyID can be null, if null, query all")
    @GetMapping("/getOrdersInDateRangeByCompanyID")
    public List<PurchaseOrderEntryWithProductsVO> getOrdersInDateRangeByCompanyID(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam(value = "id", defaultValue = "-1") Integer id) throws GlobalException {
        logger.info("GET Request to /purchaseOrder/getOrdersInDateRangeByCompanyID, start date: " +
                startDateString + ", end date： " + endDateString + ", id: " + id);

        Date startDate = MyUtils.parseAndCheckDateString(startDateString);
        Date endDate = MyUtils.parseAndCheckDateString(endDateString);

        return purchaseOrderService.getOrdersInDateRangeByCompanyID(startDate, endDate, id);
    }

    @ApiOperation(value = "", response = void.class)
    @PatchMapping("/modifyOrder")
    public void modifyOrder(@RequestBody @Validated PurchaseOrderModifyVO modificationVO) {
        logger.info("PATCH Request to /purchaseOrder/modifyOrder");

        purchaseOrderService.modifyOrder(modificationVO);
    }

    @ApiOperation(value = "", response = void.class)
    @DeleteMapping("/deleteOrder/{id}")
    public void deleteOrder(@PathVariable("id") String id) {
        logger.info("DELETE Request to /purchaseOrder/deleteOrder, id: " + id);

        purchaseOrderService.deleteOrder(id);
    }
}
