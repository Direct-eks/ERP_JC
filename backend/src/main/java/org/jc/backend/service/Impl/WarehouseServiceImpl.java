package org.jc.backend.service.Impl;

import org.jc.backend.dao.WarehouseMapper;
import org.jc.backend.entity.WarehouseO;
import org.jc.backend.service.WarehouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseServiceImpl.class);

    private final WarehouseMapper warehouseMapper;

    public WarehouseServiceImpl(WarehouseMapper warehouseMapper) {
        this.warehouseMapper = warehouseMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    public List<WarehouseO> getWarehouseOptions() {
        return warehouseMapper.queryWarehouses();
    }
}
