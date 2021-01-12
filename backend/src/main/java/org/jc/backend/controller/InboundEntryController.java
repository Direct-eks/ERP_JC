package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.config.exception.GlobalException;
import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.VO.InboundEntryWithProductsVO;
import org.jc.backend.service.InboundEntryService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Api(tags = "InboundEntry Related")
@RestController
@RequestMapping("/inboundEntry")
public class InboundEntryController {
    private static final Logger logger = LoggerFactory.getLogger(InboundEntryController.class);

    private final InboundEntryService inboundEntryService;

    public InboundEntryController(InboundEntryService inboundEntryService) {
        this.inboundEntryService = inboundEntryService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = void.class)
    @PutMapping("/createEntry")
    public void createEntry(@RequestBody @Validated InboundEntryWithProductsVO inboundEntryVO) {
        logger.info("PUT Request to /inboundEntry/createEntry");

        inboundEntryService.createEntry(inboundEntryVO);
    }

    @ApiOperation(value = "", response = InboundEntryWithProductsVO.class)
    @GetMapping("/getEntriesInDateRange")
    public List<InboundEntryWithProductsVO> getEntriesInDateRange(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam(value = "companyID", defaultValue = "-1") int companyID,
            @RequestParam("type") String type,
            @RequestParam("forModify") boolean forModify
    ) throws GlobalException {
        logger.info("GET Request to /inboundEntry/getEntriesInDateRange, start date: " +
                startDateString + ", end date： " + endDateString + ", companyID: " + companyID +
                ", type: " + type + ", forModify:" + forModify);

        Date startDate = MyUtils.parseAndCheckDateString(startDateString);
        Date endDate = MyUtils.parseAndCheckDateString(endDateString);

        switch (type) {
            case "购入":
            case "出退":
                break;
            default:
                throw new GlobalException("Invalid type param");
        }

        List<InboundEntryWithProductsVO> entries = inboundEntryService.getEntriesInDateRangeByTypeAndCompanyID(
                startDate, endDate, type, companyID);

        //forbid changes to invoiced entries
        //drop entries if were invoiced when forModify is true
        if (forModify) {
            entries.removeIf(entry -> {
                for (var product : entry.getInboundProducts()) {
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
    public void completeEntry(@RequestBody @Validated InboundEntryWithProductsVO inboundEntryWithProductsVO) {
        logger.info("PATCH Request to /inboundEntry/completeEntry");

        inboundEntryService.completeEntry(inboundEntryWithProductsVO);
    }

    @ApiOperation(value = "", response = void.class)
    @PatchMapping("/modifyEntry")
    public void modifyEntry(@RequestBody @Validated InboundEntryWithProductsVO inboundEntryWithProductsVO) {
        logger.info("PATCH Request to /inboundEntry/modifyEntry");

        inboundEntryService.modifyEntry(inboundEntryWithProductsVO);
    }

    @ApiOperation(value = "", response = void.class)
    @DeleteMapping("/deleteEntry/{id}")
    public void deleteEntry(@PathVariable("id") String id) {
        logger.info("DELETE Request to /inboundEntry/deleteEntry, id: " + id);

        inboundEntryService.deleteEntry(id);
    }

    @ApiOperation(value = "", response = void.class)
    @PostMapping("/returnEntryProducts")
    public void returnEntryProducts(@RequestBody @Validated InboundEntryWithProductsVO inboundEntryWithProductsVO) {
        logger.info("DELETE Request to /inboundEntry/returnEntryProducts");

        inboundEntryService.returnEntry(inboundEntryWithProductsVO);
    }

    @ApiOperation(value = "", response = InboundProductO.class)
    @GetMapping("/getNotCheckedOutProducts")
    public List<InboundProductO> getNotCheckedOutProducts(
            @RequestParam("companyID") int companyID,
            @RequestParam("invoiceType") String invoiceType
    ) {
        logger.info("GET Request to /inboundEntry/getNotCheckedOutProducts, companyID: " +
                companyID + ", invoiceType: " + invoiceType);

        return inboundEntryService.getNotCheckedOutProducts(companyID, invoiceType);
    }

    @ApiOperation(value = "", response = InboundProductO.class)
    @GetMapping("/getCheckoutAndNotInvoicedProducts")
    public List<InboundProductO> getCheckoutAndNotInvoicedProducts(
            @RequestParam("companyID") int companyID,
            @RequestParam("invoiceType") String invoiceType
    ) {
        logger.info("GET Request to /inboundEntry/getCheckoutAndNotInvoicedProducts, companyID: " +
                companyID + ", invoiceType: " + invoiceType);

        return inboundEntryService.getCheckoutButNotInvoicedProducts(companyID, invoiceType);
    }
}
