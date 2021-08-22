package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.ModificationO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface ModificationMapper {
    void insertEntryModificationRecord(ModificationO modificationO);
    void insertMiscModificationRecord(ModificationO modificationO);
    List<ModificationO> getEntryRecordsBySerial(String serial);
    List<ModificationO> getMiscRecordsBySerial(String category, int id);
}
