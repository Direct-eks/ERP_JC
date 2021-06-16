package org.jc.backend.service;

import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.OutboundProductO;
import org.jc.backend.entity.StatO.EntryProductVO;
import org.jc.backend.entity.WarehouseStockO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface WarehouseStockService {
    List<WarehouseStockO> getWarehouseStocksBySku(int id);
    int insertNewWarehouseStock(WarehouseStockO warehouseStockO);
    WarehouseStockO getWarehouseStockByWarehouseAndSku(int warehouse, int sku);
    int passPresaleCheck(List<InboundProductO> inboundProducts);

    void increaseStockAndUpdateStockUnitPrice(InboundProductO product);
    void increaseStockAndUpdateStockUnitPrice(InboundProductO modifiedProduct,
                                              InboundProductO originProduct);
    void decreaseStock(OutboundProductO product);
    void decreaseStock(OutboundProductO modifiedProduct, OutboundProductO originProduct);

    List<EntryProductVO> getProductsByWarehouseStockID(int id);
}
