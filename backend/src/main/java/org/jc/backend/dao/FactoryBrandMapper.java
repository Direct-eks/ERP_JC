package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.FactoryBrandO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FactoryBrandMapper {
    List<FactoryBrandO> queryAllFactoryBrands();
}
