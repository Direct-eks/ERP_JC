package org.jc.backend.service;

import org.jc.backend.entity.VO.CheckoutEntryWithProductsVO;

import java.util.Date;
import java.util.List;

public interface CheckoutEntryService {
    void createEntry(CheckoutEntryWithProductsVO checkoutEntryWithProductsVO,
                     boolean isInbound, boolean isReturn);
    List<CheckoutEntryWithProductsVO> getEntriesInDateRange(boolean isInbound,Date startDate, Date endDate,
                                                            int companyID, String invoiceType);
    void modifyEntry(CheckoutEntryWithProductsVO modifyVO);
}
