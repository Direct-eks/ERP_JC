package org.jc.backend.service;

import org.jc.backend.entity.FactoryBrandO;
import org.jc.backend.entity.VO.ListUpdateVO;

import java.util.List;

public interface FactoryBrandService {
    List<FactoryBrandO> getAllFactoryBrands();

    void updateAllBrands(ListUpdateVO<FactoryBrandO> updateVO);
}
