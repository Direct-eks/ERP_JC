package org.jc.backend.service;

import org.jc.backend.entity.VO.WarehouseEntryWithProductsVO;
import org.springframework.stereotype.Indexed;

import java.util.Date;
import java.util.List;

@Indexed
public interface WarehouseEntryService {
    void createEntry(WarehouseEntryWithProductsVO entry, String type, boolean isInbound);
    List<WarehouseEntryWithProductsVO> getEntriesInDateRange(Date startDate, Date endDate, String type, boolean isInbound);
    void modifyEntry(WarehouseEntryWithProductsVO entry, String type, boolean isInbound);

    void getAllProductsByWarehouseStockID(int id, String type, boolean isInbound);
}
