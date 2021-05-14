package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.SkuMapper;
import org.jc.backend.entity.ModelO;
import org.jc.backend.entity.SkuFullO;
import org.jc.backend.service.ModelService;
import org.jc.backend.service.SkuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkuServiceImpl implements SkuService {

    private static final Logger logger = LoggerFactory.getLogger(SkuServiceImpl.class);

    private final SkuMapper skuMapper;
    private final ModelService modelService;

    public SkuServiceImpl(SkuMapper skuMapper,
                          ModelService modelService) {
        this.skuMapper = skuMapper;
        this.modelService = modelService;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional(readOnly = true)
    @Override
    public List<SkuFullO> getFullSkuByModel(int id) {
        try {
            return skuMapper.queryFullSkuByModel(id);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<SkuFullO> getSkusByCategoryAndFactoryBrand(int modelCategoryID, int factoryBrandID) {
        try {
            List<ModelO> models;
            if (modelCategoryID != -1) {
                models = modelService.getModelsByCoarseCategoryID(modelCategoryID);
            }
            else {
                models = modelService.getAllModels();
            }

            List<SkuFullO> skus = new ArrayList<>();
            models.forEach(model ->
                    skus.addAll(skuMapper.queryFullSkuByModelAndFactoryBrandID(model.getModelID(), factoryBrandID)));

            return skus;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void modifySkuPricing(SkuFullO skuFullO) {
        try {
            skuMapper.updateSkuPricing(skuFullO);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

}
