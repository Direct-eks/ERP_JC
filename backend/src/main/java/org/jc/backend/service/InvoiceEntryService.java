package org.jc.backend.service;

import org.jc.backend.entity.VO.CheckoutEntryWithProductsVO;

public interface InvoiceEntryService {
    void createEntryForCheckout(CheckoutEntryWithProductsVO checkoutEntryWithProductsVO, boolean isInbound);
}
