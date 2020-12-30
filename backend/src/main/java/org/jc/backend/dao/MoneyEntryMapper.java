package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.MoneyEntryO;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MoneyEntryMapper {
    int countNumberOfEntriesOfToday(String prefix);
    void insertEntry(MoneyEntryO moneyEntryO);
}
