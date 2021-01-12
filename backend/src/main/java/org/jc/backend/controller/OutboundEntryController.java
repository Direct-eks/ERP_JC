package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.OutboundProductO;
import org.jc.backend.entity.VO.OutboundEntryWithProductsVO;
import org.jc.backend.service.OutboundEntryService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

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
    @PutMapping("/createEntry")
    public void createEntry(@RequestBody @Validated OutboundEntryWithProductsVO inboundEntryVO) throws GlobalParamException {
        logger.info("PUT Request to /outboundEntry/createEntry");

        outboundEntryService.createEntry(inboundEntryVO);
    }

    @ApiOperation(value = "", response = OutboundEntryWithProductsVO.class)
    @GetMapping("/getEntriesInDateRange")
    public List<OutboundEntryWithProductsVO> getEntriesInDateRange(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam(value = "companyID", defaultValue = "-1") int companyID,
            @RequestParam("type") String type,
            @RequestParam("forModify") boolean forModify
    ) throws GlobalParamException {
        logger.info("GET Request to /outboundEntry/getEntriesInDateRange, start date: " +
                startDateString + ", end date： " + endDateString + ", companyID: " + companyID +
                ", type: " + type + ", forModify: " + forModify);

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
    @PatchMapping("/completeEntry")
    public void completeEntry(@RequestBody @Validated OutboundEntryWithProductsVO outboundEntryWithProductsVO) {
        logger.info("PATCH Request to /outboundEntry/completeEntry");

        outboundEntryService.completeEntry(outboundEntryWithProductsVO);
    }

    @ApiOperation(value = "", response = void.class)
    @PatchMapping("/modifyEntry")
    public void modifyEntry(@RequestBody @Validated OutboundEntryWithProductsVO outboundEntryWithProductsVO) {
        logger.info("PATCH Request to /outboundEntry/modifyEntry");

        outboundEntryService.modifyEntry(outboundEntryWithProductsVO);
    }

    @ApiOperation(value = "", response = void.class)
    @DeleteMapping("/deleteEntry/{id}")
    public void deleteEntry(@PathVariable("id") String id) {
        logger.info("DELETE Request to /outboundEntry/deleteEntry, id: " + id);

        outboundEntryService.deleteEntry(id);
    }

    @ApiOperation(value = "", response = void.class)
    @PostMapping("/returnEntryProducts")
    public void returnEntryProducts(@RequestBody @Validated OutboundEntryWithProductsVO outboundEntryWithProductsVO) {
        logger.info("DELETE Request to /outboundEntry/returnEntryProducts");

        outboundEntryService.returnEntry(outboundEntryWithProductsVO);
    }

    @ApiOperation(value = "", response = OutboundProductO.class)
    @GetMapping("/getNotCheckedOutProducts")
    public List<OutboundProductO> getNotCheckedOutProducts(
            @RequestParam("companyID") int companyID,
            @RequestParam("invoiceType") String invoiceType
    ) {
        logger.info("GET Request to /outboundEntry/getNotCheckedOutProducts, companyID: " +
                companyID + ", invoiceType: " + invoiceType);

        return outboundEntryService.getNotCheckedOutProducts(companyID, invoiceType);
    }

    @ApiOperation(value = "", response = OutboundProductO.class)
    @GetMapping("/getCheckoutAndNotInvoicedProducts")
    public List<OutboundProductO> getCheckoutAndNotInvoicedProducts(
            @RequestParam("companyID") int companyID,
            @RequestParam("invoiceType") String invoiceType
    ) {
        logger.info("GET Request to /outboundEntry/getCheckoutAndNotInvoicedProducts, companyID: " +
                companyID + ", invoiceType: " + invoiceType);

        return outboundEntryService.getCheckoutButNotInvoicedProducts(companyID, invoiceType);
    }

    @ApiOperation(value = "", response = OutboundEntryWithProductsVO.class)
    @GetMapping("/getEntriesByCompanyAndShippingCostType")
    public List<OutboundEntryWithProductsVO> getEntriesByCompanyAndShippingCostType(
            @RequestParam("companyID") int companyID,
            @RequestParam("shippingCostType") String shippingCostType
    ) throws GlobalParamException {
        logger.info("GET Request to /outboundEntry/getEntriesByCompanyAndShippingCostType, companyID: " +
                companyID + ", shippingCostType: " + shippingCostType);

        switch (shippingCostType) {
            case "自付":
            case "代垫":
                break;
            default:
                throw new GlobalParamException("Invalid shippingCostType param");
        }

        return outboundEntryService.getEntriesByCompanyAndShippingCostType(companyID, shippingCostType);
    }
}
