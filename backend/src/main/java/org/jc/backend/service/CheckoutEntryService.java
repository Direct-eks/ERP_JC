package org.jc.backend.service;

import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.StatO.CheckoutSummaryO;
import org.jc.backend.entity.StatO.OutboundSpecialSummaryO;
import org.jc.backend.entity.VO.CheckoutEntryWithProductsVO;
import org.springframework.stereotype.Indexed;

import java.util.Date;
import java.util.List;

@Indexed
public interface CheckoutEntryService {
    void createEntry(CheckoutEntryWithProductsVO checkoutEntryWithProductsVO,
                     boolean isInbound, boolean isReturn) throws GlobalParamException;
    List<CheckoutEntryWithProductsVO> getEntriesInDateRange(boolean isInbound,Date startDate, Date endDate,
                                                            int companyID, String invoiceType);
    void modifyEntry(CheckoutEntryWithProductsVO modifyVO);
    void returnEntry(CheckoutEntryWithProductsVO returnVO, boolean isInbound) throws GlobalParamException;

    List<CheckoutSummaryO> getCheckoutSummary(boolean accurate, boolean isInbound, int companyID, String startDate, String endDate,
                                              int categoryID, String factoryBrand, int warehouseID, int departmentID);
    List<OutboundSpecialSummaryO> getCheckoutSummaryByParentCategory(String startDate, String endDate);
    List<OutboundSpecialSummaryO> getCheckoutSummaryBySubCategory(String startDate, String endDate, int id);
    List<OutboundSpecialSummaryO> getCheckoutSummaryByBrand(String startDate, String endDate);
    List<OutboundSpecialSummaryO> getCheckoutSummaryByCompany(String startDate, String endDate);

    List<CheckoutSummaryO> getPayableSummary(int companyID) throws GlobalParamException;
    List<CheckoutSummaryO> getReceivableSummary(int companyID) throws GlobalParamException;

    void auditEntriesByMonth(boolean isInbound, String month) throws GlobalParamException;
    void deleteAuditMonth(String month, String value) throws GlobalParamException;

    List<CheckoutSummaryO> getDiffStat(int companyID, String startDate, String endDate,
                                       int categoryID, String factoryBrand, int departmentID);
}
