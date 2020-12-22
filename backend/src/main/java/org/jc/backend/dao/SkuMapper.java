package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.SkuWithFactoryBrandO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SkuMapper {
    List<SkuWithFactoryBrandO> queryFactoryBrandsByModel(int id);
}
