package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.StatO.StockStatO;
import org.jc.backend.entity.WarehouseStockO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface WarehouseStockMapper {
    List<WarehouseStockO> queryWarehouseStocksBySku(int id);
    int insertNewWarehouseStock(WarehouseStockO warehouseStockO);

    WarehouseStockO queryWarehouseStockByWarehouseAndSku(int warehouse, int sku);
    WarehouseStockO queryWarehouseStockByID(int warehouseStockID);
    void updateStockInfo(WarehouseStockO warehouseStockO);
    void updateInitialInfo(WarehouseStockO warehouseStockO);

    List<StockStatO> queryWarehouseStocks(String treeLevel, int warehouseID, String factoryBrand, String code);
}
