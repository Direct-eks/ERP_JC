package org.jc.backend.service;

import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.VO.InboundEntryWithProductsVO;

import java.util.Date;
import java.util.List;

public interface InboundEntryService {
    void createEntry(InboundEntryWithProductsVO entryWithProductsVO);
    List<InboundEntryWithProductsVO> getEntriesInDateRangeByTypeAndCompanyID(Date startDate, Date endDate,
                                                                             String type, int companyID);
    void completeEntry(InboundEntryWithProductsVO completionVO);
    void modifyEntry(InboundEntryWithProductsVO modificationVO);
    void deleteEntry(String id);

    List<InboundProductO> getNotCheckedOutProducts(int companyID, String invoiceType);
    List<InboundProductO> getCheckoutAndNotInvoicedProducts(int companyID, String invoiceType);

    void updateProductsWithCheckoutSerial(List<InboundProductO> products, String checkoutSerial);
    void updateProductsWithInvoiceSerial(List<InboundProductO> products, String invoiceSerial);

    List<InboundProductO> getProductsWithCheckoutSerial(String checkoutSerial);
    List<InboundProductO> getProductsWithInvoiceSerial(String invoiceSerial);
}
