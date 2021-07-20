package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.StatO.SummaryO;
import org.jc.backend.entity.VO.PurchaseOrderEntryWithProductsVO;
import org.jc.backend.service.PurchaseOrderService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Indexed
@Api(tags = "PurchaseOrder Related")
@RestController
@RequestMapping("/purchaseOrder")
public class PurchaseOrderController {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderController.class);

    public final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = void.class)
    @RequiresPermissions("purchaseOrder:Creation")
    @PutMapping("/createOrder")
    public void createOrder(@RequestBody @Validated PurchaseOrderEntryWithProductsVO purchaseOrderEntry) {
        logger.info("PUT Request to /purchaseOrder/createOrder, data: {}", purchaseOrderEntry.toString());

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
        logger.info("GET Request to /purchaseOrder/getOrdersInDateRangeByCompanyID, start date: {}; " +
                "end date: {}; id: {};", startDateString, endDateString, companyID);

        Date startDate = MyUtils.parseAndCheckDateString(startDateString);
        Date endDate = MyUtils.parseAndCheckDateString(endDateString);

        return purchaseOrderService.getOrdersInDateRangeByCompanyID(startDate, endDate, companyID);
    }

    @ApiOperation(value = "", response = PurchaseOrderEntryWithProductsVO.class)
    @RequiresPermissions("purchaseOrder:Query")
    @GetMapping("/getOrdersByCompanyID/{id}")
    public List<PurchaseOrderEntryWithProductsVO> getOrdersByCompanyID(@PathVariable("id") int id) {
        logger.info("GET Request to /purchaseOrder/getOrdersByCompanyID, id: {}", id);

        return purchaseOrderService.getOrdersByCompanyID(id);
    }

    @ApiOperation(value = "", response = void.class)
    @RequiresPermissions("purchaseOrder:Modification")
    @PatchMapping("/modifyOrder")
    public void modifyOrder(@RequestBody @Validated PurchaseOrderEntryWithProductsVO modifyEntryVO) {
        logger.info("PATCH Request to /purchaseOrder/modifyOrder, data: {}", modifyEntryVO.toString());

        purchaseOrderService.modifyOrder(modifyEntryVO);
    }

    @ApiOperation(value = "", response = void.class)
    @DeleteMapping("/deleteOrder/{id}")
    public void deleteOrder(@PathVariable("id") String id) {
        logger.info("DELETE Request to /purchaseOrder/deleteOrder, id: {}", id);

        purchaseOrderService.deleteOrder(id);
    }

    @ApiOperation(value = "", response = SummaryO.class)
    @GetMapping("/summary")
    public List<SummaryO> purchaseSummary(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam("categoryID") int categoryID,
            @RequestParam("factoryBrand") String factoryBrand,
            @RequestParam("warehouseID") int warehouseID,
            @RequestParam("departmentID") int departmentID
    ) throws GlobalParamException {

        Date startDate = MyUtils.parseAndCheckDateString(startDateString);
        Date endDate = MyUtils.parseAndCheckDateString(endDateString);

        return purchaseOrderService.getPurchaseSummary(startDate, endDate, categoryID,
                factoryBrand, warehouseID, departmentID);
    }

}
