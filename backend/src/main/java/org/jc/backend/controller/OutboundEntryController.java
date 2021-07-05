package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.OutboundProductO;
import org.jc.backend.entity.StatO.InvoiceStatVO;
import org.jc.backend.entity.VO.OutboundEntryWithProductsVO;
import org.jc.backend.service.OutboundEntryService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Indexed
@Api(tags = "OutboundEntry Related")
@RestController
@RequestMapping("/outboundEntry")
public class OutboundEntryController {
    private static final Logger logger = LoggerFactory.getLogger(OutboundEntryController.class);

    private final OutboundEntryService outboundEntryService;

    public OutboundEntryController(OutboundEntryService outboundEntryService) {
        this.outboundEntryService = outboundEntryService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = void.class)
    @RequiresPermissions("outboundEntry:Creation")
    @PutMapping("/createEntry")
    public void createEntry(@RequestBody @Validated OutboundEntryWithProductsVO entryVO) throws GlobalParamException {
        logger.info("PUT Request to /outboundEntry/createEntry, data: {}", entryVO.toString());

        for (var p : entryVO.getOutboundProducts()) {
            if (p.getQuantity() < 1) {
                throw new GlobalParamException("商品出库数量必须大于0");
            }
        }

        outboundEntryService.createEntry(entryVO);
    }

    @ApiOperation(value = "", response = OutboundEntryWithProductsVO.class)
    @RequiresPermissions("outboundEntry:Creation")
    @GetMapping("/getEntriesInDateRange")
    public List<OutboundEntryWithProductsVO> getEntriesInDateRange(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam(value = "companyID", defaultValue = "-1") int companyID,
            @RequestParam("type") String type,
            @RequestParam("forModify") boolean forModify
    ) throws GlobalParamException {
        logger.info("GET Request to /outboundEntry/getEntriesInDateRange, start date: {}; end date: {};" +
                "companyID: {}; type: {}; forModify: {};", startDateString, endDateString, companyID,
                type, forModify);

        Date startDate = MyUtils.parseAndCheckDateString(startDateString);
        Date endDate = MyUtils.parseAndCheckDateString(endDateString);

        switch (type) {
            case "销出":
            case "入退":
                break;
            default:
                throw new GlobalParamException("Invalid type error");
        }

        List<OutboundEntryWithProductsVO> entries = outboundEntryService.getEntriesInDateRange(
                startDate, endDate, type, companyID);

        if (forModify) {
            entries.removeIf(entry -> {
                for (var product : entry.getOutboundProducts()) {
                    if (!product.getCheckoutSerial().equals("")) {
                        return true;
                    }
                }
                return false;
            });
        }

        return entries;
    }

    @ApiOperation(value = "", response = void.class)
    @RequiresPermissions("outboundEntry:Completion")
    @PatchMapping("/completeEntry")
    public void completeEntry(@RequestBody @Validated OutboundEntryWithProductsVO completeEntryVO) {
        logger.info("PATCH Request to /outboundEntry/completeEntry, data: {}", completeEntryVO.toString());

        outboundEntryService.completeEntry(completeEntryVO);
    }

    @ApiOperation(value = "", response = void.class)
    @RequiresPermissions("outboundEntry:Modification")
    @PatchMapping("/modifyEntry")
    public void modifyEntry(@RequestBody @Validated OutboundEntryWithProductsVO modifyEntryVO) {
        logger.info("PATCH Request to /outboundEntry/modifyEntry, data: {}", modifyEntryVO.toString());

        outboundEntryService.modifyEntry(modifyEntryVO);
    }

    @ApiOperation(value = "", response = void.class)
    @DeleteMapping("/deleteEntry/{id}")
    public void deleteEntry(@PathVariable("id") String id) {
        logger.info("DELETE Request to /outboundEntry/deleteEntry, id: {}", id);

        outboundEntryService.deleteEntry(id);
    }

    @ApiOperation(value = "", response = void.class)
    @RequiresPermissions("outboundEntry:Return")
    @PostMapping("/returnEntryProducts")
    public void returnEntryProducts(@RequestBody @Validated OutboundEntryWithProductsVO returnEntryVO) {
        logger.info("DELETE Request to /outboundEntry/returnEntryProducts, data: {}", returnEntryVO.toString());

        outboundEntryService.returnEntry(returnEntryVO);
    }

    @ApiOperation(value = "", response = OutboundProductO.class)
    @RequiresPermissions("outboundCheckout:Creation")
    @GetMapping("/getNotCheckedOutProductsByEntryID")
    public List<OutboundProductO> getNotCheckedOutProductsByEntryID(
            @RequestParam("entryID") String entryID,
            @RequestParam("invoiceType") String invoiceType
    ) throws GlobalParamException {
        logger.info("GET Request to /outboundEntry/getNotCheckedOutProductsByEntryID," +
                "entryID: {}, invoice type: {}", entryID, invoiceType);

        if (!MyUtils.validateSerial(entryID)) {
            throw new GlobalParamException("单号格式错误");
        }

        return outboundEntryService.getNotCheckedOutProductsByEntryID(entryID, invoiceType);
    }

    @ApiOperation(value = "", response = OutboundProductO.class)
    @RequiresPermissions("outboundCheckout:Creation")
    @GetMapping("/getNotCheckedOutProducts")
    public List<OutboundProductO> getNotCheckedOutProducts(
            @RequestParam("companyID") int companyID,
            @RequestParam("invoiceType") String invoiceType
    ) {
        logger.info("GET Request to /outboundEntry/getNotCheckedOutProducts, companyID: {}; invoiceType: {};",
                companyID, invoiceType);

        return outboundEntryService.getNotCheckedOutProducts(companyID, invoiceType);
    }

    @ApiOperation(value = "", response = OutboundProductO.class)
    @RequiresPermissions("outboundInvoice:Creation")
    @GetMapping("/getCheckoutAndNotInvoicedProductsByEntryID")
    public List<OutboundProductO> getCheckoutAndNotInvoicedProductsByEntryID(
            @RequestParam("entryID") String entryID,
            @RequestParam("invoiceType") String invoiceType) throws GlobalParamException {
        logger.info("GET Request to /outboundEntry/getCheckoutAndNotInvoicedProductsByEntryID, " +
                "entryID: {}, invoiceType: {}", entryID, invoiceType);

        if (!MyUtils.validateSerial(entryID)) {
            throw new GlobalParamException("单号格式错误");
        }

        return outboundEntryService.getCheckoutButNotInvoicedProductsByEntryID(entryID, invoiceType);
    }

    @ApiOperation(value = "", response = OutboundProductO.class)
    @RequiresPermissions("outboundInvoice:Creation")
    @GetMapping("/getCheckoutAndNotInvoicedProducts")
    public List<OutboundProductO> getCheckoutAndNotInvoicedProducts(
            @RequestParam("companyID") int companyID,
            @RequestParam("invoiceType") String invoiceType
    ) {
        logger.info("GET Request to /outboundEntry/getCheckoutAndNotInvoicedProducts, companyID: {}; invoiceType: {};",
                companyID, invoiceType);

        return outboundEntryService.getCheckoutButNotInvoicedProducts(companyID, invoiceType);
    }

    @ApiOperation(value = "", response = OutboundEntryWithProductsVO.class)
    @RequiresPermissions("outboundShippingCost:Creation")
    @GetMapping("/getEntriesByCompanyAndShippingCostType")
    public List<OutboundEntryWithProductsVO> getEntriesByCompanyAndShippingCostType(
            @RequestParam("companyID") int companyID,
            @RequestParam("shippingCostType") String shippingCostType
    ) throws GlobalParamException {
        logger.info("GET Request to /outboundEntry/getEntriesByCompanyAndShippingCostType, companyID: {}; " +
                "shippingCostType: {};", companyID, shippingCostType);

        switch (shippingCostType) {
            case "自付":
            case "代垫":
                break;
            default:
                throw new GlobalParamException("Invalid shippingCostType param");
        }

        return outboundEntryService.getEntriesByCompanyAndShippingCostType(companyID, shippingCostType);
    }

    @ApiOperation(value = "", response = InvoiceStatVO.class)
    @RequiresPermissions("outboundCheckout:NotCheckoutQuery")
    @GetMapping("/getNotYetCheckoutSummary")
    public List<InvoiceStatVO> getNotYetCheckoutSummary() {
        logger.info("GET Request to /outboundEntry/getNotYetCheckoutSummary");

        return outboundEntryService.getNotYetCheckoutSummary();
    }

    @ApiOperation(value = "", response = OutboundProductO.class)
    @RequiresPermissions("outboundCheckout:NotCheckoutQuery")
    @GetMapping("/getNotYetCheckoutDetailByCompanyID/{companyID}")
    public List<OutboundProductO> getNotYetCheckoutDetailByCompanyID(@PathVariable("companyID") int companyID) {
        logger.info("GET Request to /outboundEntry/getNotYetCheckoutDetailByCompanyID, companyID: {}", companyID);

        return outboundEntryService.getNotYetCheckoutDetailByCompanyID(companyID);
    }

    @ApiOperation(value = "", response = InvoiceStatVO.class)
    @RequiresPermissions("outboundInvoice:NotInvoiceQuery")
    @GetMapping("/getNotYetInvoiceSummary")
    public List<InvoiceStatVO> getNotYetInvoiceSummary() {
        logger.info("GET Request to /outboundEntry/getNotYetInvoiceSummary");

        return outboundEntryService.getNotYetInvoiceSummary();
    }

    @ApiOperation(value = "", response = OutboundProductO.class)
    @RequiresPermissions("outboundInvoice:NotInvoiceQuery")
    @GetMapping("/getNotYetInvoiceDetailByCompanyID/{companyID}")
    public List<OutboundProductO> getNotYetInvoiceDetailByCompanyID(@PathVariable("companyID") int companyID) {
        logger.info("GET Request to /outboundEntry/getNotYetInvoiceDetailByCompanyID, companyID: {}", companyID);

        return outboundEntryService.getNotYetInvoiceDetailByCompanyID(companyID);
    }
}
