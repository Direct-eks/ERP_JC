package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.MoneyEntryO;
import org.jc.backend.entity.StatO.MoneyEntryDetailO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface MoneyEntryMapper {
    int countNumberOfEntriesOfToday(String date, String prefix);
    void insertEntry(MoneyEntryO moneyEntryO);
    List<MoneyEntryO> getEntriesInDateRangeAndParams(String startDate, String endDate, int companyID,
                                                     String paymentMethod, int bankAccountID, String prefix);
    MoneyEntryO selectEntryBySerial(String serial);
    void modifyEntry(MoneyEntryO moneyEntryO);

    List<MoneyEntryO> queryAllEntriesByPrefixAndCompany(String prefix, int id);
    void updateEntryDetailBySerial(MoneyEntryDetailO entry);
}
