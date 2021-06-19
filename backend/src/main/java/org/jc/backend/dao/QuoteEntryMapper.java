package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.DO.QuotaEntryDO;
import org.jc.backend.entity.QuoteProductO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface QuoteEntryMapper {
    int countNumberOfEntriesOfToday();
    void insertNewOrderEntry(QuotaEntryDO entry);
    int insertNewOrderProduct(QuoteProductO product);

    List<QuotaEntryDO> queryEntriesInDateRangeByCompanyID(String startDate, String endDate, int id);
    List<QuotaEntryDO> queryEntriesByCompanyID(int id);
    List<QuoteProductO> queryProductsByEntryID(String id);

    QuotaEntryDO selectEntryForCompare(String id);
    List<QuoteProductO> selectProductsForCompare(String id);
    void updateOrderEntry(QuotaEntryDO entryModifyDO);
    void updateOrderProduct(QuoteProductO productModifyDO);
    void deleteOrderProductByID(int id);

    void deleteOrderEntry(String id);
    void deleteOrderProductsByEntryID(String id);
}
