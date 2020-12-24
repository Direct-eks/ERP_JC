package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.DO.ModificationDO;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ModificationMapper {
    void insertModificationRecord(ModificationDO modificationDO);
}
