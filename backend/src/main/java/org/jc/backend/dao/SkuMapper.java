package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.SkuFullO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SkuMapper {
    List<SkuFullO> queryFullSkuByModel(int id);
}
