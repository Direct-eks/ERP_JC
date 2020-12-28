package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.ModificationO;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ModificationMapper {
    void insertModificationRecord(ModificationO modificationO);
}
