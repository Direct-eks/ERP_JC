package org.jc.backend.service;

import org.jc.backend.entity.WarehouseStockO;

import java.util.List;

public interface WarehouseStockService {
    List<WarehouseStockO> getWarehouseStocksBySku(int id);
}
