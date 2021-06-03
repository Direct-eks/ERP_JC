package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.DO.PurchaseOrderEntryDO;
import org.jc.backend.entity.PurchaseOrderProductO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface PurchaseOrderMapper {

    int countNumberOfEntriesOfToday();
    void insertNewOrderEntry(PurchaseOrderEntryDO entry);
    int insertNewOrderProduct(PurchaseOrderProductO product);

    List<PurchaseOrderEntryDO> queryEntriesInDateRangeByCompanyID(String startDate, String endDate, int id);
    List<PurchaseOrderEntryDO> queryEntriesByCompanyID(int id);
    List<PurchaseOrderProductO> queryProductsByEntryID(String id);

    PurchaseOrderEntryDO selectEntryForCompare(String id);
    List<PurchaseOrderProductO> selectProductsForCompare(String id);
    void updateOrderEntry(PurchaseOrderEntryDO entryModifyDO);
    void updateOrderProduct(PurchaseOrderProductO productModifyDO);
    void deleteOrderProductByID(int id);

    void deleteOrderEntry(String id);
    void deleteOrderProductsByEntryID(String id);
}
