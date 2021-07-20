package org.jc.backend.service;

import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.DO.InboundEntryDO;
import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.StatO.InboundSummaryO;
import org.jc.backend.entity.StatO.InvoiceStatVO;
import org.jc.backend.entity.StatO.ProductStatO;
import org.jc.backend.entity.VO.InboundEntryWithProductsVO;
import org.springframework.stereotype.Indexed;

import java.util.Date;
import java.util.List;

@Indexed
public interface InboundEntryService {
    String createEntry(InboundEntryWithProductsVO entryWithProductsVO) throws GlobalParamException;
    List<InboundEntryWithProductsVO> getEntriesInDateRangeByTypeAndCompanyID(Date startDate, Date endDate,
                                                                             String type, int companyID);
    void completeEntry(InboundEntryWithProductsVO completionVO);
    void modifyEntry(InboundEntryWithProductsVO modificationVO);
    void deleteEntry(String id);
    void returnEntry(InboundEntryWithProductsVO returnVO);

    List<InboundProductO> getNotCheckedOutProductsByEntryID(String entryID, String invoiceType) throws GlobalParamException;
    List<InboundProductO> getNotCheckedOutProducts(int companyID, String invoiceType);
    List<InboundProductO> getCheckoutButNotInvoicedProductsByEntryID(String entryID, String invoiceType) throws GlobalParamException;
    List<InboundProductO> getCheckoutButNotInvoicedProducts(int companyID, String invoiceType);

    void updateProductsWithCheckoutSerial(List<InboundProductO> products, String checkoutSerial);
    void updateProductsWithInvoiceSerial(List<InboundProductO> products, String invoiceSerial);

    List<InboundProductO> getProductsWithCheckoutSerial(String checkoutSerial);
    List<InboundProductO> getProductsWithInvoiceSerial(String invoiceSerial);

    void updateEntryWithShippingCostSerial(InboundEntryDO inboundEntryDO);
    List<InboundEntryDO> getEntriesWithShippingCostSerial(String shippingCostSerial);

    List<InboundEntryWithProductsVO> getEntriesByCompanyAndShippingCostType(int companyID, String shippingCostType);

    List<InvoiceStatVO> getNotYetCheckoutSummary();
    List<InboundProductO> getNotYetCheckoutDetailByCompanyID(int companyID);
    List<InvoiceStatVO> getNotYetInvoiceSummary();
    List<InboundProductO> getNotYetInvoiceDetailByCompanyID(int companyID);

    List<InboundProductO> getProductsByWarehouseID(int id);

    // for stock price calculation
    List<ProductStatO> getAllInboundProducts(int id);
    void updateInboundProduct(ProductStatO productO);

    List<InboundSummaryO> getInboundSummary(String type, Date startDate, Date endDate, int categoryID,
                                            String factoryBrand, int warehouseID, int departmentID);
}
