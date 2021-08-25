package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.DO.FeeEntryDO;
import org.jc.backend.entity.FeeCategoryO;
import org.jc.backend.entity.FeeEntryDetailO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface FeesMapper {
    List<FeeCategoryO> queryAllCategories();
    void insertCategory(FeeCategoryO categoryO);
    void updateCategory(FeeCategoryO categoryO);
    void deleteCategory(int id);

    int countNumberOfEntriesOfGivenDate(String date, String prefix);
    void insertNewEntry(FeeEntryDO entryDO);
    void insertNewDetail(FeeEntryDetailO entryDetailO);

    List<FeeEntryDO> queryEntriesInDateRange(String startDate, String endDate,
                                             String prefix);
    List<FeeEntryDetailO> queryDetailsByEntryID(String id);

    FeeEntryDO selectEntryForCompare(String id);
    void updateEntry(FeeEntryDO entryDO);
    void updateEntryDetail(FeeEntryDetailO entryDetailO);

}
