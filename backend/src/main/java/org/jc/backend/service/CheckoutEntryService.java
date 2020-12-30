package org.jc.backend.service;

import org.jc.backend.entity.VO.CheckoutEntryWithProductsVO;

public interface CheckoutEntryService {
    void createEntry(CheckoutEntryWithProductsVO checkoutEntryWithProductsVO);
}
