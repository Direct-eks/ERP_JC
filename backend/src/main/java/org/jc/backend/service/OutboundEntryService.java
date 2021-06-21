package org.jc.backend.service;

import org.jc.backend.entity.DO.OutboundEntryDO;
import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.OutboundProductO;
import org.jc.backend.entity.StatO.InvoiceStatVO;
import org.jc.backend.entity.VO.OutboundEntryWithProductsVO;
import org.springframework.stereotype.Indexed;

import java.util.Date;
import java.util.List;

@Indexed
public interface OutboundEntryService {
    void createEntry(OutboundEntryWithProductsVO entryWithProductsVO);
    boolean passPresaleDateCheck(List<InboundProductO> products, String entryDate);
    void replenishPresaleProducts(List<InboundProductO> products);

    List<OutboundEntryWithProductsVO> getEntriesInDateRange(Date startDate, Date endDate,
                                                            String type, int id);
    void completeEntry(OutboundEntryWithProductsVO outboundEntryWithProductsVO);
    void modifyEntry(OutboundEntryWithProductsVO outboundEntryWithProductsVO);
    void deleteEntry(String id);
    void returnEntry(OutboundEntryWithProductsVO returnVO);

    List<OutboundProductO> getNotCheckedOutProducts(int companyID, String invoiceType);
    List<OutboundProductO> getCheckoutButNotInvoicedProducts(int companyID, String invoiceType);

    void updateProductsWithCheckoutSerial(List<OutboundProductO> products, String checkoutSerial);
    void updateProductsWithInvoiceSerial(List<OutboundProductO> products, String invoiceSerial);

    List<OutboundProductO> getProductsWithCheckoutSerial(String checkoutSerial);
    List<OutboundProductO> getProductsWithInvoiceSerial(String invoiceSerial);

    void updateEntryWithShippingCostSerial(OutboundEntryDO outboundEntryDO);
    List<OutboundEntryDO> getEntriesWithShippingCostSerial(String shippingCostSerial);

    List<OutboundEntryWithProductsVO> getEntriesByCompanyAndShippingCostType(
            int companyID, String shippingCostType);

    List<InvoiceStatVO> getNotYetCheckoutSummary();
    List<OutboundProductO> getNotYetCheckoutDetailByCompanyID(int companyID);
    List<InvoiceStatVO> getNotYetInvoiceSummary();
    List<OutboundProductO> getNotYetInvoiceDetailByCompanyID(int companyID);

    List<OutboundProductO> getProductsByWarehouseID(int id);
}
