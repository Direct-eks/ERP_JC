package org.jc.backend.service.Impl;

import org.jc.backend.dao.WarehouseEntryMapper;
import org.jc.backend.entity.VO.WarehouseEntryWithProductsVO;
import org.jc.backend.service.WarehouseEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ProductionEntryServiceImpl implements WarehouseEntryService {

    private static final Logger logger = LoggerFactory.getLogger(ProductionEntryServiceImpl.class);

    private final WarehouseEntryMapper warehouseEntryMapper;

    public ProductionEntryServiceImpl(WarehouseEntryMapper warehouseEntryMapper) {
        this.warehouseEntryMapper = warehouseEntryMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    @Override
    public void createEntry(WarehouseEntryWithProductsVO entryVO) {

    }

    @Override
    public List<WarehouseEntryWithProductsVO> getEntriesInDateRange(Date startDate, Date endDate) {
        return null;
    }

    @Override
    public void modifyEntry(WarehouseEntryWithProductsVO entry) {

    }
}
