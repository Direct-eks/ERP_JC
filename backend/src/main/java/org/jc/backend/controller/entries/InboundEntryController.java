package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.StatO.InboundSummaryO;
import org.jc.backend.entity.StatO.InvoiceStatVO;
import org.jc.backend.entity.VO.InboundEntryWithProductsVO;
import org.jc.backend.service.InboundEntryService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Indexed
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
    @RequiresPermissions("inboundEntry:Creation")
    @PutMapping("/createEntry")
    public void createEntry(@RequestBody @Validated InboundEntryWithProductsVO inboundEntryVO) throws GlobalParamException {
        logger.info("PUT Request to /inboundEntry/createEntry, data: {}", inboundEntryVO.toString());

        for (var p : inboundEntryVO.getInboundProducts()) {
            if (p.getQuantity() < 1) {
                throw new GlobalParamException("商品入库数量必须大于0");
            }
        }

        inboundEntryService.createEntry(inboundEntryVO);
    }

    @ApiOperation(value = "", response = InboundEntryWithProductsVO.class)
    @RequiresPermissions("inboundEntry:Query")
    @GetMapping("/getEntriesInDateRange")
    public List<InboundEntryWithProductsVO> getEntriesInDateRange(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam(value = "companyID", defaultValue = "-1") int companyID,
            @RequestParam("type") String type,
            @RequestParam("forModify") boolean forModify
    ) throws GlobalParamException {
        logger.info("GET Request to /inboundEntry/getEntriesInDateRange, start date: {}; " +
                "end date: {}; companyID: {}; type: {}; forModify: {};",
                startDateString, endDateString, companyID, type, forModify);

        Date startDate = MyUtils.parseAndCheckDateString(startDateString);
        Date endDate = MyUtils.parseAndCheckDateString(endDateString);

        switch (type) {
            case "购入":
            case "出退":
                break;
            default:
                throw new GlobalParamException("Invalid type param");
        }

        List<InboundEntryWithProductsVO> entries = inboundEntryService.getEntriesInDateRangeByTypeAndCompanyID(
                startDate, endDate, type, companyID);

        //forbid changes to invoiced entries if subject has no admin role
        //drop entries if were invoiced when forModify is true
        if (forModify) {
            Subject subject = SecurityUtils.getSubject();
            if (!subject.hasRole("admin")) {
                entries.removeIf(entry -> {
                    for (var product : entry.getInboundProducts()) {
                        if (!product.getCheckoutSerial().equals("")) {
                            return true;
                        }
                    }
                    return false;
                });
            }
        }

        return entries;
    }

    @ApiOperation(value = "", response = void.class)
    @RequiresPermissions("inboundEntry:Completion")
    @PatchMapping("/completeEntry")
    public void completeEntry(@RequestBody @Validated InboundEntryWithProductsVO completeEntryVO) {
        logger.info("PATCH Request to /inboundEntry/completeEntry, data: {}", completeEntryVO.toString());

        inboundEntryService.completeEntry(completeEntryVO);
    }

    @ApiOperation(value = "", response = void.class)
    @RequiresPermissions("inboundEntry:Modification")
    @PatchMapping("/modifyEntry")
    public void modifyEntry(@RequestBody @Validated InboundEntryWithProductsVO modifyEntryVO) {
        logger.info("PATCH Request to /inboundEntry/modifyEntry, data: {}", modifyEntryVO.toString());

        inboundEntryService.modifyEntry(modifyEntryVO);
    }

    @ApiOperation(value = "", response = void.class)
    @DeleteMapping("/deleteEntry/{id}")
    public void deleteEntry(@PathVariable("id") String id) {
        logger.info("DELETE Request to /inboundEntry/deleteEntry, id: {}", id);

        inboundEntryService.deleteEntry(id);
    }

    @ApiOperation(value = "", response = void.class)
    @RequiresPermissions("inboundEntry:Return")
    @PostMapping("/returnEntryProducts")
    public void returnEntryProducts(@RequestBody @Validated InboundEntryWithProductsVO returnEntryVO) {
        logger.info("DELETE Request to /inboundEntry/returnEntryProducts, data: {}", returnEntryVO.toString());

        inboundEntryService.returnEntry(returnEntryVO);
    }

    @ApiOperation(value = "", response = InboundProductO.class)
    @RequiresPermissions("inboundCheckout:Creation")
    @GetMapping("/getNotCheckedOutProductsByEntryID")
    public List<InboundProductO> getNotCheckedOutProductsByEntryID(
            @RequestParam("entryID") String entryID,
            @RequestParam("invoiceType") String invoiceType
    ) throws GlobalParamException {
        logger.info("GET Request to /inboundEntry/getNotCheckedOutProductsByEntryID," +
                "entryID: {}, invoice type: {}", entryID, invoiceType);

        if (!MyUtils.validateSerial(entryID)) {
            throw new GlobalParamException("单号格式错误");
        }

        return inboundEntryService.getNotCheckedOutProductsByEntryID(entryID, invoiceType);
    }

    @ApiOperation(value = "", response = InboundProductO.class)
    @RequiresPermissions("inboundCheckout:Creation")
    @GetMapping("/getNotCheckedOutProducts")
    public List<InboundProductO> getNotCheckedOutProducts(
            @RequestParam("companyID") int companyID,
            @RequestParam("invoiceType") String invoiceType
    ) {
        logger.info("GET Request to /inboundEntry/getNotCheckedOutProducts, companyID: {}; invoiceType: {};",
                companyID, invoiceType);

        return inboundEntryService.getNotCheckedOutProducts(companyID, invoiceType);
    }

    @ApiOperation(value = "", response = InboundProductO.class)
    @RequiresPermissions("inboundInvoice:Creation")
    @GetMapping("/getCheckoutAndNotInvoicedProductsByEntryID")
    public List<InboundProductO> getCheckoutAndNotInvoicedProductsByEntryID(
            @RequestParam("entryID") String entryID,
            @RequestParam("invoiceType") String invoiceType) throws GlobalParamException {
        logger.info("GET Request to /inboundEntry/getCheckoutAndNotInvoicedProductsByEntryID, " +
                "entryID: {}, invoiceType: {}", entryID, invoiceType);

        if (!MyUtils.validateSerial(entryID)) {
            throw new GlobalParamException("单号格式错误");
        }

        return inboundEntryService.getCheckoutButNotInvoicedProductsByEntryID(entryID, invoiceType);
    }

    @ApiOperation(value = "", response = InboundProductO.class)
    @RequiresPermissions("inboundInvoice:Creation")
    @GetMapping("/getCheckoutAndNotInvoicedProducts")
    public List<InboundProductO> getCheckoutAndNotInvoicedProducts(
            @RequestParam("companyID") int companyID,
            @RequestParam("invoiceType") String invoiceType
    ) {
        logger.info("GET Request to /inboundEntry/getCheckoutAndNotInvoicedProducts, companyID: {}; " +
                "invoiceType: {};", companyID, invoiceType);

        return inboundEntryService.getCheckoutButNotInvoicedProducts(companyID, invoiceType);
    }

    @ApiOperation(value = "", response = InboundEntryWithProductsVO.class)
    @RequiresPermissions("inboundShippingCost:Creation")
    @GetMapping("/getEntriesByCompanyAndShippingCostType")
    public List<InboundEntryWithProductsVO> getEntriesByCompanyAndShippingCostType(
            @RequestParam("companyID") int companyID,
            @RequestParam("shippingCostType") String shippingCostType
    ) throws GlobalParamException {
        logger.info("GET Request to /inboundEntry/getEntriesByCompanyAndShippingCostType, companyID: {}; " +
                "shippingCostType: {};", companyID, shippingCostType);

        switch (shippingCostType) {
            case "自付": // company will always be selfCompany
            case "代垫":
                break;
            default:
                throw new GlobalParamException("Invalid shippingCostType param");
        }

        return inboundEntryService.getEntriesByCompanyAndShippingCostType(companyID, shippingCostType);
    }

    @ApiOperation(value = "", response = InvoiceStatVO.class)
    @RequiresPermissions("inboundCheckout:NotCheckoutQuery")
    @GetMapping("/getNotYetCheckoutSummary")
    public List<InvoiceStatVO> getNotYetCheckoutSummary() {
        logger.info("GET Request to /inboundEntry/getNotYetCheckoutSummary");

        return inboundEntryService.getNotYetCheckoutSummary();
    }

    @ApiOperation(value = "", response = InboundProductO.class)
    @RequiresPermissions("inboundCheckout:NotCheckoutQuery")
    @GetMapping("/getNotYetCheckoutDetailByCompanyID/{companyID}")
    public List<InboundProductO> getNotYetCheckoutDetailByCompanyID(@PathVariable("companyID") int companyID) {
        logger.info("GET Request to /inboundEntry/getNotYetCheckoutDetailByCompanyID, companyID: {}", companyID);

        return inboundEntryService.getNotYetCheckoutDetailByCompanyID(companyID);
    }

    @ApiOperation(value = "", response = InvoiceStatVO.class)
    @RequiresPermissions("inboundInvoice:NotInvoiceQuery")
    @GetMapping("/getNotYetInvoiceSummary")
    public List<InvoiceStatVO> getNotYetInvoiceSummary() {
        logger.info("GET Request to /inboundEntry/getNotYetInvoiceSummary");

        return inboundEntryService.getNotYetInvoiceSummary();
    }

    @ApiOperation(value = "", response = InboundProductO.class)
    @RequiresPermissions("inboundInvoice:NotInvoiceQuery")
    @GetMapping("/getNotYetInvoiceDetailByCompanyID/{companyID}")
    public List<InboundProductO> getNotYetInvoiceDetailByCompanyID(@PathVariable("companyID") int companyID) {
        logger.info("GET Request to /inboundEntry/getNotYetInvoiceDetailByCompanyID, companyID: {}", companyID);

        return inboundEntryService.getNotYetInvoiceDetailByCompanyID(companyID);
    }

    @ApiOperation(value = "", response = InboundSummaryO.class)
    @GetMapping("/getInboundSummary")
    public List<InboundSummaryO> getInboundSummary(
            @RequestParam("type") String type,
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam("categoryID") int categoryID,
            @RequestParam("factoryBrand") String factoryBrand,
            @RequestParam("warehouseID") int warehouseID,
            @RequestParam("departmentID") int departmentID
    ) throws GlobalParamException {
        logger.info("GET Request to /inboundEntry/getInboundSummary");

        switch (type) {
            case "购入":
            case "出退":
                break;
            default: throw new GlobalParamException("invalid category");
        }

        Date startDate = MyUtils.parseAndCheckDateString(startDateString);
        Date endDate = MyUtils.parseAndCheckDateString(endDateString);

        return inboundEntryService.getInboundSummary(type, startDate, endDate, categoryID,
                factoryBrand, warehouseID, departmentID);
    }
}
