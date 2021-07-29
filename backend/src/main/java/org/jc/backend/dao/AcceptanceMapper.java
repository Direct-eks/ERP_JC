package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.AcceptanceEntryO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface AcceptanceMapper {
    int countNumberOfEntriesOfGivenDate(String date, String type);
    void insertEntry(AcceptanceEntryO entryO);

    AcceptanceEntryO queryEntryByID(String id);
    List<AcceptanceEntryO> queryEntriesInDateRange(String startDate, String endDate, String type);
    void updateEntry(AcceptanceEntryO entryO);
    void deleteEntry(String id);
}
