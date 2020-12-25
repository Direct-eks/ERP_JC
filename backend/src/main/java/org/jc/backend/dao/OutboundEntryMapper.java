package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.DO.OutboundEntryDO;
import org.jc.backend.entity.DO.OutboundEntryModifyDO;
import org.jc.backend.entity.OutboundEntryCompleteO;
import org.jc.backend.entity.OutboundProductModifyO;
import org.jc.backend.entity.OutboundProductO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OutboundEntryMapper {
    int countNumberOfEntriesOfToday();
    void insertNewEntry(OutboundEntryDO entryDO);
    int insertNewProduct(OutboundProductO productO);

    List<OutboundEntryDO> queryEntriesInDateRangeByCompanyID(String startDate, String endDate, int id);
    List<OutboundProductO> queryProductsByEntryID(String id);

    List<OutboundEntryCompleteO> selectEntryShippingInfoForCompare(String id);
    void updateShippingInfo(OutboundEntryCompleteO completeO);

    List<OutboundEntryModifyDO> selectEntryForCompare(String id);
    List<OutboundProductModifyO> selectProductsForCompare(String id);
    void updateEntry(OutboundEntryModifyDO outboundEntryModifyDO);
    void updateProduct(OutboundProductModifyO outboundProductModifyO);
    void deleteProductByID(int id);

    void deleteEntry(String id);
    void deleteProductsByEntryID(String id);
}
