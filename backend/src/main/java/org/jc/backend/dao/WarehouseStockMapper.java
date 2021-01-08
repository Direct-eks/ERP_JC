package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.WarehouseStockO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface WarehouseStockMapper {
    List<WarehouseStockO> queryWarehouseStocksBySku(int id);
    int insertNewWarehouseStock(WarehouseStockO warehouseStockO);

    WarehouseStockO queryWarehouseStockByWarehouseAndSku(int warehouse, int sku);
}
