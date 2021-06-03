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
    void insertModificationRecord(ModificationO modificationO);
    List<ModificationO> getRecordsBySerial(String serial);
}
