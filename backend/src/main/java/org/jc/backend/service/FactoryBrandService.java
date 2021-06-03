package org.jc.backend.service;

import org.jc.backend.entity.FactoryBrandO;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface FactoryBrandService {
    List<FactoryBrandO> getAllFactoryBrands();

    void updateAllBrands(ListUpdateVO<FactoryBrandO> updateVO);
}
