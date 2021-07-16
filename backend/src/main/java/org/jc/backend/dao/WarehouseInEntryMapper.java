package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.DO.WarehouseEntryDO;
import org.jc.backend.entity.StatO.ProductStatO;
import org.jc.backend.entity.WarehouseProductO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface WarehouseInEntryMapper {
    int countNumberOfEntriesOfGivenDateAndType(String date, String type);
    void insertNewEntry(WarehouseEntryDO entryDO);
    int insertNewProduct(WarehouseProductO productO);

    List<WarehouseEntryDO> queryEntriesInDateRangeByType(String startDate, String endDate, String type);
    List<WarehouseProductO> queryProductsByEntryID(String id);

    WarehouseEntryDO selectEntryForCompare(String id);
    List<WarehouseProductO> selectProductsForCompare(String id);
    void updateEntry(WarehouseEntryDO entryDO);
    void updateProduct(WarehouseProductO productO);

    List<ProductStatO> queryAllInboundProductsByWarehouseStockID(int id);
    void updateProductStockInfo(ProductStatO productStatO);
}
