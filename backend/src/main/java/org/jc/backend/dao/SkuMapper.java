package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.SkuFullO;
import org.jc.backend.entity.SkuO;
import org.jc.backend.entity.StatO.StockLimitO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface SkuMapper {
    List<SkuFullO> queryFullSkuByModel(int id);

    List<SkuFullO> querySkuByCoarseCategoryAndFactoryBrand(String treeLevel, int factoryBrandID);

    void updateSkuPricing(SkuFullO skuFullO);
    void updateStockInfo(int id, int quantity);

    void insertSku(SkuO skuO);
    void updateSku(SkuO skuO);
    void deleteSku(int id);

    List<StockLimitO> queryStockAlert();
}
