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
    int countNumberOfEntriesOfGivenDate(String date, String classification);
    void insertEntry(AcceptanceEntryO entryO);

    AcceptanceEntryO queryEntryBySerial(String serial);
    void updateClassification(String serial, String classification);

    List<AcceptanceEntryO> queryEntryByNumber(String number);
    List<AcceptanceEntryO> queryEntriesInDateRange(String startDate, String endDate, String prefix);
    void updateEntry(AcceptanceEntryO entryO);
    void deleteEntry(String id);

    List<AcceptanceEntryO> queryAllEntriesByPrefixAndCompany(String prefix, int id);
}
