package org.jc.backend.service;

import org.jc.backend.entity.SkuFullO;

import java.util.List;

public interface SkuService {
    List<SkuFullO> getFullSkuByModel(int id);

    List<SkuFullO> getSkusByModelCategoryAndFactoryBrand(int modelCategoryID, int factoryBrandID);

    void modifySkuPricing(SkuFullO skuFullO);
}
