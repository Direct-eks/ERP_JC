package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.DO.PurchaseOrderEntryDO;
import org.jc.backend.entity.PurchaseOrderProductO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PurchaseOrderMapper {

    int countNumberOfEntries();
    void insertNewEntry(PurchaseOrderEntryDO entry);
    int insertNewProduct(PurchaseOrderProductO product);

    List<PurchaseOrderEntryDO> queryEntriesWithinDateRangeByCompanyID(
            String startDate, String endDate, int id);
    List<PurchaseOrderProductO> queryProductsByEntryID(String id);
}
