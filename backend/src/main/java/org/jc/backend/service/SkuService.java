package org.jc.backend.service;

import org.jc.backend.entity.ModelO;
import org.jc.backend.entity.SkuFullO;
import org.jc.backend.entity.VO.ListUpdateVO;

import java.util.List;

public interface SkuService {
    List<SkuFullO> getFullSkuByModel(int id);

    List<SkuFullO> getSkusByCategoryAndFactoryBrand(int modelCategoryID, int factoryBrandID);

    void modifySkuPricing(List<SkuFullO> skuFullO);

    void createNewSkus(List<ModelO> models, int[] brandIDs);
    void updateSku(int modelID, ListUpdateVO<SkuFullO> updateVO);
}
