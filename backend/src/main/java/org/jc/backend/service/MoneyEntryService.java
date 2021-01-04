package org.jc.backend.service;

import org.jc.backend.entity.DO.CheckoutEntryDO;

public interface MoneyEntryService {
    String createEntryForCheckout(CheckoutEntryDO checkoutEntry, String checkoutSerial, boolean isInbound);
}
