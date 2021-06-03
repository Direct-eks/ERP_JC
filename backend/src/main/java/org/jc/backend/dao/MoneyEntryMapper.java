package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.MoneyEntryO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface MoneyEntryMapper {
    int countNumberOfEntriesOfToday(String prefix);
    void insertEntry(MoneyEntryO moneyEntryO);
    List<MoneyEntryO> getEntriesInDateRangeAndParams(String startDate, String endDate, int companyID,
                                                     String paymentMethod, int bankAccountID, String prefix);
    MoneyEntryO selectEntryBySerial(String serial);
    void modifyEntry(MoneyEntryO moneyEntryO);
}
