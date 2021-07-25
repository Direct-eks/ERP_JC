package org.jc.backend.service;

import org.jc.backend.entity.FactoryBrandO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface FactoryBrandService {
    List<FactoryBrandO> getAllFactoryBrands();

    void updateAllBrands(List<FactoryBrandO> updateVO);
}
