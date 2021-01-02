package org.jc.backend.service;

import org.jc.backend.entity.VO.CheckoutEntryWithProductsVO;

import java.util.Date;
import java.util.List;

public interface CheckoutEntryService {
    void createEntry(CheckoutEntryWithProductsVO checkoutEntryWithProductsVO);
    List<CheckoutEntryWithProductsVO> getEntriesInDateRange(Date startDate, Date endDate,
                                                            int companyID, String invoiceType);
    void modifyEntry(CheckoutEntryWithProductsVO modifyVO);
}
