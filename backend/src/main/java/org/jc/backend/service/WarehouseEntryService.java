package org.jc.backend.service;

import org.jc.backend.entity.VO.WarehouseEntryWithProductsVO;
import org.springframework.stereotype.Indexed;

import java.util.Date;
import java.util.List;

@Indexed
public interface WarehouseEntryService {
    void createEntry(WarehouseEntryWithProductsVO entry);
    List<WarehouseEntryWithProductsVO> getEntriesInDateRange(Date startDate, Date endDate);
    void modifyEntry(WarehouseEntryWithProductsVO entry);
}
