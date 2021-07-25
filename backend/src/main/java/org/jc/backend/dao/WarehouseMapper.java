package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.WarehouseO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface WarehouseMapper {
    List<WarehouseO> queryWarehouses();

    void insertWarehouse(WarehouseO warehouseO);
    void updateWarehouse(WarehouseO warehouseO);
    void deleteWarehouse(int id);
}
