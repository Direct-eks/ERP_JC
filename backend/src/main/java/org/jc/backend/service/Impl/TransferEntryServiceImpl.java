package org.jc.backend.service.Impl;

import org.jc.backend.entity.VO.WarehouseEntryWithProductsVO;
import org.jc.backend.service.WarehouseEntryService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("transferEntryService")
public class TransferEntryServiceImpl implements WarehouseEntryService {

    @Override
    public void createEntry(WarehouseEntryWithProductsVO entry) {

    }

    @Override
    public List<WarehouseEntryWithProductsVO> getEntriesInDateRange(Date startDate, Date endDate) {
        return null;
    }

    @Override
    public void modifyEntry(WarehouseEntryWithProductsVO entry) {

    }
}
