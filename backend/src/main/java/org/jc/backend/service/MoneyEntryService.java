package org.jc.backend.service;

import org.jc.backend.entity.DO.CheckoutEntryDO;
import org.jc.backend.entity.MoneyEntryO;

import java.util.Date;
import java.util.List;

public interface MoneyEntryService {
    void createEntry(MoneyEntryO moneyEntryO, boolean isInbound);
    List<MoneyEntryO> getEntriesInDateRange(Date startDate, Date endDate, int companyID,
                                            String paymentMethod, int bankAccountID, boolean isInbound);
    List<MoneyEntryO> getEntryBySerial(String serialSuffix, boolean isInbound);
    void modifyEntry(MoneyEntryO moneyEntryO);

    String createEntryForCheckout(CheckoutEntryDO checkoutEntry, String checkoutSerial, boolean isInbound);
}
