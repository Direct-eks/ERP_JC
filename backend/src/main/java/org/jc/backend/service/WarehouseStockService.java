package org.jc.backend.service;

import org.jc.backend.entity.WarehouseStockO;

import java.util.List;

public interface WarehouseStockService {
    WarehouseStockO getWarehouseStocksBySku(int id);
}
