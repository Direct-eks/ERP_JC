package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.MiscellaneousDataO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface MiscellaneousDataMapper {
    List<MiscellaneousDataO> queryAllTaxRateOptions();
}
