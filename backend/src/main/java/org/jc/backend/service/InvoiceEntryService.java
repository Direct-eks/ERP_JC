package org.jc.backend.service;

import org.jc.backend.entity.InvoiceEntryO;
import org.jc.backend.entity.VO.CheckoutEntryWithProductsVO;
import org.jc.backend.entity.VO.InvoiceEntryStandAloneVO;

import java.util.Date;
import java.util.List;

public interface InvoiceEntryService {
    void createEntry(InvoiceEntryStandAloneVO invoiceEntryStandAloneVO, boolean isInbound);
    List<InvoiceEntryStandAloneVO> getEntriesInDateRange(Date startDate, Date endDate, Date invoiceDate, int companyID,
                                              int isFollowUpIndication, String invoiceNumber, String invoiceType);
    void modifyEntry(InvoiceEntryStandAloneVO invoiceEntryStandAloneVO);

    String createEntryForCheckout(CheckoutEntryWithProductsVO checkoutEntryWithProductsVO, boolean isInbound);
}
