package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.DO.PurchaseOrderEntryDO;
import org.jc.backend.entity.DO.PurchaseOrderEntryModifyDO;
import org.jc.backend.entity.DO.PurchaseOrderProductModifyDO;
import org.jc.backend.entity.PurchaseOrderProductO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PurchaseOrderMapper {

    int countNumberOfEntriesOfToday();
    void insertNewOrderEntry(PurchaseOrderEntryDO entry);
    int insertNewOrderProduct(PurchaseOrderProductO product);

    List<PurchaseOrderEntryDO> queryEntriesInDateRangeByCompanyID(String startDate, String endDate, int id);
    List<PurchaseOrderProductO> queryProductsByEntryID(String id);

    List<PurchaseOrderEntryModifyDO> selectEntryForCompare(String id);
    List<PurchaseOrderProductModifyDO> selectProductsForCompare(String id);
    void updateOrderEntry(PurchaseOrderEntryModifyDO entryModifyDO);
    void updateOrderProduct(PurchaseOrderProductModifyDO productModifyDO);

    void deleteOrderEntry(String id);
    void deleteOrderProducts(String id);
}
