package org.jc.backend.service;

import org.jc.backend.entity.DO.CheckoutEntryDO;

public interface MoneyEntryService {
    String createEntryForCheckout(String checkoutSerial, CheckoutEntryDO checkoutEntry);
}
