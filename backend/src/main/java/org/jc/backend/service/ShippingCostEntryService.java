package org.jc.backend.service;

import org.jc.backend.entity.VO.ShippingCostEntryVO;

import java.util.Date;
import java.util.List;

public interface ShippingCostEntryService {
    void createEntry(ShippingCostEntryVO shippingCostEntryVO, boolean isInbound);
    List<ShippingCostEntryVO> getEntriesInDateRange(Date startDate, Date endDate, int companyID, boolean isInbound);
    void modifyEntry(ShippingCostEntryVO shippingCostEntryVO);
}
