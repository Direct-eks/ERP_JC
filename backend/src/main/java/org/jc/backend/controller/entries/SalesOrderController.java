package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.StatO.SummaryO;
import org.jc.backend.entity.VO.SalesOrderEntryWithProductsVO;
import org.jc.backend.service.SalesOrderService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Indexed
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

    @ApiOperation(value = "", response = void.class)
    @RequiresPermissions("salesOrder:Creation")
    @PutMapping("/createOrder")
    public void createOrder(@RequestBody @Validated SalesOrderEntryWithProductsVO salesOrderEntryWithProductsVO) {
        logger.info("PUT Request to /salesOrder/createOrder, data: {}", salesOrderEntryWithProductsVO.toString());

        salesOrderService.createOrder(salesOrderEntryWithProductsVO);
    }

    @ApiOperation(value = "", response = SalesOrderEntryWithProductsVO.class,
            notes = "companyID can be null, if null, query all")
    @RequiresPermissions("salesOrder:Query")
    @GetMapping("/getOrdersInDateRangeByCompanyID")
    public List<SalesOrderEntryWithProductsVO> getOrdersInDateRangeByCompanyID(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam(value = "companyID", defaultValue = "-1") int companyID) throws GlobalParamException {
        logger.info("GET Request to /salesOrder/getOrdersInDateRangeByCompanyID, start date: {}; " +
                "end date: {}; id: {};", startDateString, endDateString, companyID);

        Date startDate = MyUtils.parseAndCheckDateString(startDateString);
        Date endDate = MyUtils.parseAndCheckDateString(endDateString);

        return salesOrderService.getOrdersInDateRangeByCompanyID(startDate, endDate, companyID);
    }

    @ApiOperation(value = "", response = SalesOrderEntryWithProductsVO.class)
    @RequiresPermissions("salesOrder:Query")
    @GetMapping("/getOrdersByCompanyID/{id}")
    public List<SalesOrderEntryWithProductsVO> getOrdersByCompanyID(@PathVariable("id") int id) {
        logger.info("GET Request to /salesOrder/getOrdersByCompanyID, id: {}", id);

        return salesOrderService.getOrdersByCompanyID(id);
    }

    @ApiOperation(value = "", response = void.class)
    @RequiresPermissions("salesOrder:Modification")
    @PatchMapping("/modifyOrder")
    public void modifyOrder(@RequestBody @Validated SalesOrderEntryWithProductsVO modifyOrderVO) {
        logger.info("PATCH Request to /salesOrder/modifyOrder, data: {}", modifyOrderVO.toString());

        salesOrderService.modifyOrder(modifyOrderVO);
    }

    @ApiOperation(value = "", response = void.class)
    @DeleteMapping("/deleteOrder/{id}")
    public void deleteOrder(@PathVariable("id") String id) {
        logger.info("DELETE Request to /salesOrder/deleteOrder, id: " + id);

        salesOrderService.deleteOrder(id);
    }

    @ApiOperation(value = "", response = SummaryO.class)
    @GetMapping("/summary")
    public List<SummaryO> salesSummary(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam("categoryID") int categoryID,
            @RequestParam("factoryBrand") String factoryBrand,
            @RequestParam("warehouseID") int warehouseID,
            @RequestParam("departmentID") int departmentID
    ) throws GlobalParamException {

        Date startDate = MyUtils.parseAndCheckDateString(startDateString);
        Date endDate = MyUtils.parseAndCheckDateString(endDateString);

        return salesOrderService.getSalesSummary(startDate, endDate, categoryID,
                factoryBrand, warehouseID, departmentID);
    }

}
