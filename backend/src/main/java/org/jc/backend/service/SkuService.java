package org.jc.backend.service;

import org.jc.backend.entity.SkuWithFactoryBrandO;

import java.util.List;

public interface SkuService {
    List<SkuWithFactoryBrandO> getFactoryBrandsByModel(int id);
}
