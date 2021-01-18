package org.jc.backend.service;

import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.StatO.EntryProductVO;
import org.jc.backend.entity.WarehouseStockO;

import java.util.List;

public interface WarehouseStockService {
    List<WarehouseStockO> getWarehouseStocksBySku(int id);
    int insertNewWarehouseStock(WarehouseStockO warehouseStockO);
    WarehouseStockO getWarehouseStockByWarehouseAndSku(int warehouse, int sku);
    void updateWarehouseStockUnitPriceAndQuantity(InboundProductO product);
    void updateWarehouseStockUnitPriceAndQuantity(InboundProductO modifiedProduct,
                                                  InboundProductO originProduct);

    List<EntryProductVO> getProductsByWarehouseStockID(int id);
}
