package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.StatO.CheckoutSummaryO;
import org.jc.backend.entity.StatO.EntryStatO;
import org.jc.backend.entity.StatO.OutboundSpecialSummaryO;
import org.jc.backend.entity.VO.CheckoutEntryWithProductsVO;
import org.jc.backend.service.CheckoutEntryService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Indexed
@Api(tags = "CheckoutEntry Related")
@RestController
@RequestMapping("/checkoutEntry")
public class CheckoutEntryController {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutEntryController.class);

    private final CheckoutEntryService checkoutEntryService;

    public CheckoutEntryController(CheckoutEntryService checkoutEntryService) {
        this.checkoutEntryService = checkoutEntryService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = void.class)
    @RequiresPermissions(value = {"inboundCheckout:Creation", "outboundCheckout:Creation"}, logical = Logical.OR)
    @PutMapping("/createEntry")
    public void createEntry(@RequestBody @Validated CheckoutEntryWithProductsVO checkoutEntryWithProductsVO,
                            @RequestParam("isInbound") boolean isInbound,
                            @RequestParam(value = "isReturn", defaultValue = "false") boolean isReturn
    ) throws GlobalParamException {
        logger.info("PUT Request to /checkoutEntry/createEntry， isInbound: {}; data: {}", isInbound,
                checkoutEntryWithProductsVO.toString());

        checkoutEntryService.createEntry(checkoutEntryWithProductsVO, isInbound, isReturn);
    }

    @ApiOperation(value = "", response = CheckoutEntryWithProductsVO.class)
    @RequiresPermissions(value = {"inboundCheckout:Query", "outboundCheckout:Query"}, logical = Logical.OR)
    @GetMapping("/getEntriesInDateRange")
    public List<CheckoutEntryWithProductsVO> getEntriesInDateRange(
            @RequestParam("isInbound") boolean isInbound,
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam(value = "companyID", defaultValue = "-1") int companyID,
            @RequestParam(value = "invoiceType", defaultValue = "") String invoiceType,
            @RequestParam("forModify") boolean forModify
    ) throws GlobalParamException {
        logger.info("GET Request to /checkoutEntry/getEntriesInDateRange, isInbound: {}; startDate: {}; " +
                "endDate: {}; companyID: {}; invoiceType: {};", isInbound, startDateString, endDateString,
                companyID, invoiceType);

        Date startDate = MyUtils.parseAndCheckDateString(startDateString);
        Date endDate = MyUtils.parseAndCheckDateString(endDateString);

        if (StringUtils.hasLength(invoiceType)) {
            switch (invoiceType) {
                case "增值税票":
                case "普票":
                case "收据":
                    break;
                default:
                    throw new GlobalParamException("Invalid invoiceType param");
            }
        }

        List<CheckoutEntryWithProductsVO> entries = checkoutEntryService.getEntriesInDateRange(
                isInbound, startDate, endDate, companyID, invoiceType);

        //filter out verified entries, if query is forModify
        if (forModify) {
            entries.removeIf(entry -> entry.getIsVerified() == 1);
        }

        return entries;
    }

    @ApiOperation(value = "", response = void.class)
    @RequiresPermissions(value = {"inboundCheckout:Modification", "outboundCheckout:Modification"},
            logical = Logical.OR)
    @PatchMapping("/modifyEntry")
    public void modifyEntry(@RequestBody @Validated CheckoutEntryWithProductsVO modifyVO) {
        logger.info("PATCH Request to /checkoutEntry/modifyEntry, data: {}", modifyVO.toString());

        checkoutEntryService.modifyEntry(modifyVO);
    }

    @ApiOperation(value = "", response = void.class)
    @RequiresPermissions(value = {"inboundCheckout:Return", "outboundCheckout:Return"}, logical = Logical.OR)
    @PostMapping("/returnEntry")
    public void returnEntry(
            @RequestBody @Validated CheckoutEntryWithProductsVO returnVO,
            @RequestParam("isInbound") boolean isInbound
    ) throws GlobalParamException {
        logger.info("POST Request to /checkoutEntry/returnEntry, isInbound: {}; data: {}",
                isInbound, returnVO.toString());

        checkoutEntryService.returnEntry(returnVO, isInbound);
    }


    @ApiOperation(value = "", response = CheckoutSummaryO.class)
    @GetMapping("/summary")
    public List<CheckoutSummaryO> getSummary(
            @RequestParam("isInbound") boolean isInbound,
            @RequestParam("companyID") int companyID,
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam("categoryID") int categoryID,
            @RequestParam("factoryBrand") String factoryBrand,
            @RequestParam("warehouseID") int warehouseID,
            @RequestParam("departmentID") int departmentID
    ) throws GlobalParamException {
        logger.info("POST Request to /checkoutEntry/summary");

        MyUtils.parseAndCheckDateString(startDateString);
        MyUtils.parseAndCheckDateString(endDateString);

        return checkoutEntryService.getCheckoutSummary(false, isInbound, companyID, startDateString,
                endDateString, categoryID, factoryBrand, warehouseID, departmentID);
    }

    @ApiOperation(value = "", response = CheckoutSummaryO.class)
    @GetMapping("/summaryByParams")
    public List<OutboundSpecialSummaryO> getSummaryByParams(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam("type") String type,
            @RequestParam(value = "id", defaultValue = "-1") int id
    ) throws GlobalParamException {
        logger.info("POST Request to /checkoutEntry/summaryByParams");

        MyUtils.parseAndCheckDateString(startDateString);
        MyUtils.parseAndCheckDateString(endDateString);

        switch (type) {
            case "parentCategory":
                return checkoutEntryService.getCheckoutSummaryByParentCategory(startDateString, endDateString);
            case "subCategory":
                return checkoutEntryService.getCheckoutSummaryBySubCategory(startDateString, endDateString, id);
            case "brand":
                return checkoutEntryService.getCheckoutSummaryByBrand(startDateString, endDateString);
            case "company":
                return checkoutEntryService.getCheckoutSummaryByCompany(startDateString, endDateString);
            default:
                throw new GlobalParamException("invalid type");
        }
    }

    @ApiOperation(value = "", response = EntryStatO.class)
    @GetMapping("/getAuditEntries")
    public List<CheckoutSummaryO> getAuditEntries(
            @RequestParam("month") String month,
            @RequestParam("isInbound") boolean isInbound
    ) throws GlobalParamException {
        logger.info("GET Request to /checkoutEntry/getAuditEntries, month: {}", month);
        var pair = MyUtils.getFirstAndLastDayOfMonth(month);

        return checkoutEntryService.getCheckoutSummary(true, isInbound, -1, pair.getLeft(),
                pair.getRight(), -1, "", -1, -1);
    }

    @ApiOperation(value = "", response = void.class)
    @PostMapping("/auditEntries")
    public void auditEntries(
            @RequestParam("month") String month,
            @RequestParam("isInbound") boolean isInbound
    ) throws GlobalParamException {
        logger.info("GET Request to /checkoutEntry/auditEntries, month: {}", month);

        checkoutEntryService.auditEntriesByMonth(isInbound, month);
    }

    @ApiOperation(value = "", response = void.class)
    @RequiresAuthentication
    @DeleteMapping("/deleteAuditMonth")
    public void deleteAuditMonth(
            @RequestParam("month") String month,
            @RequestParam("value") String value
    ) throws GlobalParamException {
        logger.info("DELETE Request to /checkoutEntry/deleteAuditMonth, month: {}, " +
                "value: {}", month, value);

        checkoutEntryService.deleteAuditMonth(month, value);
    }

    @ApiOperation(value = "", response = CheckoutSummaryO.class)
    @RequiresAuthentication
    @GetMapping("/diffStat")
    public List<CheckoutSummaryO> getDiffStat(
            @RequestParam("companyID") int companyID,
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam("categoryID") int categoryID,
            @RequestParam("factoryBrand") String factoryBrand,
            @RequestParam("departmentID") int departmentID
    ) throws GlobalParamException {
        logger.info("GET Request to /checkoutEntry/diffStat");

        MyUtils.parseAndCheckDateString(startDateString);
        MyUtils.parseAndCheckDateString(endDateString);

        return checkoutEntryService.getDiffStat(companyID, startDateString, endDateString,
                categoryID, factoryBrand, departmentID);
    }
}
