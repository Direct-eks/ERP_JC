package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.VO.PurchaseOrderEntryWithProductsVO;
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
    @RequiresPermissions("purchaseOrder:Creation")
    @PutMapping("/createOrder")
    public void createOrder(@RequestBody @Validated PurchaseOrderEntryWithProductsVO purchaseOrderEntry) {
        logger.info("PUT Request to /purchaseOrder/createOrder");

        purchaseOrderService.createOrder(purchaseOrderEntry);
    }

    @ApiOperation(value = "", response = PurchaseOrderEntryWithProductsVO.class,
        notes = "companyID can be null, if null, query all")
    @RequiresPermissions("purchaseOrder:Query")
    @GetMapping("/getOrdersInDateRangeByCompanyID")
    public List<PurchaseOrderEntryWithProductsVO> getOrdersInDateRangeByCompanyID(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam(value = "companyID", defaultValue = "-1") int companyID) throws GlobalParamException {
        logger.info("GET Request to /purchaseOrder/getOrdersInDateRangeByCompanyID, start date: " +
                startDateString + ", end date： " + endDateString + ", id: " + companyID);

        Date startDate = MyUtils.parseAndCheckDateString(startDateString);
        Date endDate = MyUtils.parseAndCheckDateString(endDateString);

        return purchaseOrderService.getOrdersInDateRangeByCompanyID(startDate, endDate, companyID);
    }

    @ApiOperation(value = "", response = PurchaseOrderEntryWithProductsVO.class)
    @RequiresPermissions("purchaseOrder:Query")
    @GetMapping("/getOrdersByCompanyID/{id}")
    public List<PurchaseOrderEntryWithProductsVO> getOrdersByCompanyID(@PathVariable("id") int id) {
        logger.info("GET Request to /purchaseOrder/getOrdersByCompanyID, id: " + id);

        return purchaseOrderService.getOrdersByCompanyID(id);
    }

    @ApiOperation(value = "", response = void.class)
    @RequiresPermissions("purchaseOrder:Modification")
    @PatchMapping("/modifyOrder")
    public void modifyOrder(@RequestBody @Validated PurchaseOrderEntryWithProductsVO purchaseOrderEntryWithProductsVO) {
        logger.info("PATCH Request to /purchaseOrder/modifyOrder");

        purchaseOrderService.modifyOrder(purchaseOrderEntryWithProductsVO);
    }

    @ApiOperation(value = "", response = void.class)
    @DeleteMapping("/deleteOrder/{id}")
    public void deleteOrder(@PathVariable("id") String id) {
        logger.info("DELETE Request to /purchaseOrder/deleteOrder, id: " + id);

        purchaseOrderService.deleteOrder(id);
    }

}
