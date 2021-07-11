package org.jc.backend.service;

import org.jc.backend.entity.ModelO;
import org.jc.backend.entity.SkuFullO;
import org.jc.backend.entity.SkuO;
import org.jc.backend.entity.StatO.StockLimitO;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface SkuService {
    List<SkuFullO> getFullSkuByModel(int id);

    List<SkuFullO> getSkusByCategoryAndFactoryBrand(int modelCategoryID, int factoryBrandID);

    void modifySkuPricing(List<SkuFullO> skuFullO);
    void updateStockInfo(int skuID);

    void createNewSkus(List<ModelO> models, int[] brandIDs);
    void updateSku(int modelID, ListUpdateVO<SkuO> updateVO);
    void updateSkuBulk(int[] modelIDs, int[] brandIDs);

    List<StockLimitO> getStockAlert();
}
