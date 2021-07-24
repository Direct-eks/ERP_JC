package org.jc.backend.service;

import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.OutboundProductO;
import org.jc.backend.entity.StatO.EntryProductVO;
import org.jc.backend.entity.StatO.StockStatO;
import org.jc.backend.entity.WarehouseProductO;
import org.jc.backend.entity.WarehouseStockO;
import org.springframework.stereotype.Indexed;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Indexed
public interface WarehouseStockService {
    List<WarehouseStockO> getWarehouseStocksBySku(int id);
    int insertNewWarehouseStock(WarehouseStockO warehouseStockO);
    WarehouseStockO getWarehouseStockByWarehouseAndSku(int warehouse, int sku);
    int passPresaleCheck(List<InboundProductO> inboundProducts);

    void increaseStock(InboundProductO product, String date);
    void modifyStock(InboundProductO product, String date);
    void decreaseStock(OutboundProductO product, String date);
    void modifyStock(OutboundProductO product, String date);

    void increaseStock(WarehouseProductO product, String date, String type);
    void decreaseStock(WarehouseProductO product, String date, String type);
    void modifyStock(WarehouseProductO product, String date, boolean isInbound);

    List<EntryProductVO> getProductsByWarehouseStockID(int id);
    void updateWarehouseStockInitialInfo(List<WarehouseStockO> stocks);

    List<StockStatO> getWarehouseStockReport(int categoryID, int warehouseID,
                                             String factoryBrand, String code);
    void exportStockReport(HttpServletResponse response);
}
