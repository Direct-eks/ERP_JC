package org.jc.backend.service.Impl;

import org.jc.backend.dao.WarehouseStockMapper;
import org.jc.backend.entity.WarehouseStockO;
import org.jc.backend.service.WarehouseStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseStockServiceImpl implements WarehouseStockService {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseStockServiceImpl.class);

    private final WarehouseStockMapper warehouseStockMapper;

    public WarehouseStockServiceImpl(WarehouseStockMapper warehouseStockMapper) {
        this.warehouseStockMapper = warehouseStockMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    public WarehouseStockO getWarehouseStocksBySku(int id) {
        return warehouseStockMapper.queryWarehouseStocksBySku(id);
    }
}
