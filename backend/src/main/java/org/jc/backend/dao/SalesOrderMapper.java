package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.DO.SalesOrderEntryDO;
import org.jc.backend.entity.SalesOrderProductO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SalesOrderMapper {
    int countNumberOfEntriesOfToday();
    void insertNewOrderEntry(SalesOrderEntryDO entry);
    int insertNewOrderProduct(SalesOrderProductO product);

    List<SalesOrderEntryDO> queryEntriesInDateRangeByCompanyID(String startDate, String endDate, int id);
    List<SalesOrderEntryDO> queryEntriesByCompanyID(int id);
    List<SalesOrderProductO> queryProductsByEntryID(String id);

    SalesOrderEntryDO selectEntryForCompare(String id);
    List<SalesOrderProductO> selectProductsForCompare(String id);
    void updateOrderEntry(SalesOrderEntryDO entryModifyDO);
    void updateOrderProduct(SalesOrderProductO productModifyDO);
    void deleteOrderProductByID(int id);

    void deleteOrderEntry(String id);
    void deleteOrderProductsByEntryID(String id);
}
