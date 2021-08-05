package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.InitialMoneyEntryO;
import org.jc.backend.entity.StatO.MoneyEntryDetailO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface InitialMoneyEntryMapper {
    int countNumberOfEntriesOfGivenDate(String date, String prefix);
    void insertEntry(InitialMoneyEntryO o);
    List<InitialMoneyEntryO> queryEntries(String prefix);
    InitialMoneyEntryO queryEntryByCompanyID(String prefix, int id);
    void updateEntry(InitialMoneyEntryO o);
    void deleteEntry(String id);

    List<InitialMoneyEntryO> queryAllEntriesByPrefixAndCompany(String prefix, int id);
    void updateEntryDetailBySerial(MoneyEntryDetailO entry);
}
