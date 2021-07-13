package org.jc.backend.service.Impl;

import org.jc.backend.dao.WarehouseEntryMapper;
import org.jc.backend.service.WarehouseEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WarehouseEntryServiceImpl implements WarehouseEntryService {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseEntryServiceImpl.class);

    private final WarehouseEntryMapper warehouseEntryMapper;

    public WarehouseEntryServiceImpl(WarehouseEntryMapper warehouseEntryMapper) {
        this.warehouseEntryMapper = warehouseEntryMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    @Override
    public void createEntry() {

    }
}
