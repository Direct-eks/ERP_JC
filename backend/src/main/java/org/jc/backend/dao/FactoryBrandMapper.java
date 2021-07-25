package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.FactoryBrandO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface FactoryBrandMapper {
    List<FactoryBrandO> queryAllFactoryBrands();
    void insertBrand(FactoryBrandO factoryBrandO);
    void updateBrand(FactoryBrandO factoryBrandO);
    void deleteBrand(int id);
}
