package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.InitialMoneyEntryO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface InitialMoneyEntryMapper {
    void insertEntry(InitialMoneyEntryO o);
    List<InitialMoneyEntryO> queryEntries();
    void modifyEntry(InitialMoneyEntryO o);
    void deleteEntry(String id);
}
