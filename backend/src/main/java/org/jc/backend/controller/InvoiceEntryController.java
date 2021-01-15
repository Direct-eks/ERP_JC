package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.VO.InvoiceEntryStandAloneVO;
import org.jc.backend.service.InvoiceEntryService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Api(tags = "InvoiceEntry Related")
@RestController
@RequestMapping("/invoiceEntry")
public class InvoiceEntryController {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceEntryController.class);

    private final InvoiceEntryService invoiceEntryService;

    public InvoiceEntryController(InvoiceEntryService invoiceEntryService) {
        this.invoiceEntryService = invoiceEntryService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = void.class)
    @RequiresPermissions(value = {"inboundInvoice:Creation", "outboundInvoice:Creation"}, logical = Logical.OR)
    @PutMapping("/createEntry")
    public void createEntry(@RequestBody @Validated InvoiceEntryStandAloneVO invoiceEntryStandAloneVO,
                            @RequestParam("isInbound") boolean isInbound) {
        logger.info("PUT Request to /invoiceEntry/createEntry, isInbound: " + isInbound);

        invoiceEntryService.createEntry(invoiceEntryStandAloneVO, isInbound);
    }

    @ApiOperation(value = "", response = InvoiceEntryStandAloneVO.class)
    @RequiresPermissions(value = {"inboundInvoice:Query", "outboundInvoice:Query"}, logical = Logical.OR)
    @GetMapping("/getEntriesInDateRange")
    public List<InvoiceEntryStandAloneVO> getEntriesInDateRange(
            @RequestParam("isInbound") boolean isInbound,
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam(value = "invoiceNumberDate", defaultValue = "") String invoiceNumberDateString,
            @RequestParam(value = "companyID", defaultValue = "-1") int companyID,
            @RequestParam(value = "isFollowUpIndication", defaultValue = "-1") int isFollowUpIndication,
            @RequestParam(value = "invoiceNumber", defaultValue = "") String invoiceNumber,
            @RequestParam(value = "invoiceType", defaultValue = "") String invoiceType,
            @RequestParam("forModify") boolean forModify
    )throws GlobalParamException {
        logger.info("GET Request to /invoiceEntry/getEntryInDateRange, isInbound: " + isInbound + ", startDate: " +
                startDateString + ", endDate: " + endDateString + ", invoiceNumberDate: " + invoiceNumberDateString +
                ", companyID: " + companyID + ", isFollowUpIndication: " + isFollowUpIndication + ", invoiceNumber: " +
                invoiceNumber + ", invoiceType: " + invoiceType + ", forModify: " + forModify);

        Date startDate = MyUtils.parseAndCheckDateString(startDateString);
        Date endDate = MyUtils.parseAndCheckDateString(endDateString);
        Date invoiceNumberDate = invoiceNumberDateString.equals("") ? null :
                MyUtils.parseAndCheckDateString(invoiceNumberDateString);

        if (StringUtils.hasLength(invoiceType)) {
            switch (invoiceType) {
                case "增值税票":
                case "普票":
                    break;
                default:
                    throw new GlobalParamException("Invalid invoiceType param");
            }
        }

        List<InvoiceEntryStandAloneVO> entries = invoiceEntryService.getEntriesInDateRange(startDate, endDate,
                invoiceNumberDate, companyID, isFollowUpIndication, invoiceNumber, invoiceType, isInbound);

//        if (forModify) {
//            entries.removeIf(entry -> {})
//        }

        return entries;
    }

    @ApiOperation(value = "", response = void.class)
    @RequiresPermissions(value = {"inboundInvoice:Modification", "outboundInvoice:Modification"},
            logical = Logical.OR)
    @PatchMapping("/modifyEntry")
    public void modifyEntry(@RequestBody @Validated InvoiceEntryStandAloneVO invoiceEntryStandAloneVO) {
        logger.info("PATCH Request to /invoiceEntry/modifyEntry");

        invoiceEntryService.modifyEntry(invoiceEntryStandAloneVO);
    }
}
