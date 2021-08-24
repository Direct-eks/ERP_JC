package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jc.backend.dao.SkuMapper;
import org.jc.backend.entity.ModelCategoryO;
import org.jc.backend.entity.ModelO;
import org.jc.backend.entity.SkuFullO;
import org.jc.backend.entity.SkuO;
import org.jc.backend.entity.StatO.StockLimitO;
import org.jc.backend.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.jc.backend.service.ModificationRecordService.DELETION_MSG;
import static org.jc.backend.service.ModificationRecordService.SKU;

@Service
public class SkuServiceImpl implements SkuService {

    private static final Logger logger = LoggerFactory.getLogger(SkuServiceImpl.class);

    private final SkuMapper skuMapper;
    private final ModelService modelService;
    private final WarehouseStockService warehouseStockService;
    private final ModificationRecordService modificationRecordService;
    private final UsageCheckService usageCheckService;

    public SkuServiceImpl(SkuMapper skuMapper,
                          ModelService modelService,
                          WarehouseStockService warehouseStockService,
                          ModificationRecordService modificationRecordService,
                          UsageCheckService usageCheckService) {
        this.skuMapper = skuMapper;
        this.modelService = modelService;
        this.warehouseStockService = warehouseStockService;
        this.modificationRecordService = modificationRecordService;
        this.usageCheckService = usageCheckService;
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
            String treeLevel = "";
            if (modelCategoryID != -1) {
                List<ModelCategoryO> allCategories = modelService.getModelCategories();
                for (var category : allCategories) {
                    if (category.getModelCategoryID() == modelCategoryID) {
                        treeLevel = category.getTreeLevel();
                        break;
                    }
                }
            }

            return skuMapper.querySkuByCoarseCategoryAndFactoryBrand(treeLevel, factoryBrandID);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void modifySkuPricing(List<SkuFullO> skuFullO) {
        try {
            for (var modifiedSku: skuFullO) {
                skuMapper.updateSkuPricing(modifiedSku);
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateStockInfo(int skuID) {
        try {
            var stocks = warehouseStockService.getWarehouseStocksBySku(skuID);
            int quantity = 0;
            for (var stock : stocks) {
                quantity += stock.getStockQuantity();
            }
            skuMapper.updateStockInfo(skuID, quantity);
            logger.info("Updated sku stock info, id: {}", skuID);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void createNewSkus(List<ModelO> models, int[] brandIDs) {
        try {
            for (var model : models) {
                SkuO sku = new SkuO();
                sku.setModelID(model.getModelID());
                for (var brand : brandIDs) {
                    sku.setFactoryBrandID(brand);
                    skuMapper.insertSku(sku);
                    logger.info("Inserted new sku, id: {}", sku.getSkuID());
                }
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("insertion failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateSku(int modelID, List<SkuO> updateVO) {
        Subject subject = SecurityUtils.getSubject();
        String usernameString = "修改者：" + subject.getPrincipals().getPrimaryPrincipal() + "; ";

        try {
            List<SkuO> tempSkus = new ArrayList<>(updateVO);
            List<SkuO> oldSkus = skuMapper.queryFullSkuByModel(modelID).stream().map(s -> {
                SkuO skuO = new SkuO();
                BeanUtils.copyProperties(s, skuO);
                return skuO;
            }).collect(Collectors.toList());

            // check for added
            tempSkus.removeIf(s -> s.getSkuID() >= 0);
            for (var sku : tempSkus) {
                skuMapper.insertSku(sku);
                logger.info("Inserted new sku, id: {}", sku.getSkuID());
            }

            // update all
            tempSkus = new ArrayList<>(updateVO);
            tempSkus.removeIf(s -> s.getSkuID() < 0);
            for (var sku : tempSkus) {
                StringBuilder record = new StringBuilder(usernameString);
                if (sku.formModificationRecord(sku.getOldObject(oldSkus), record)) {
                    skuMapper.updateSku(sku);
                    logger.info("Updated sku, id: {}", sku.getSkuID());
                    modificationRecordService.insertRecord(SKU, sku.getSkuID(), record);
                }
            }

            // check for remove if possible
            oldSkus.removeIf(oldS -> updateVO.stream()
                    .anyMatch(s -> s.getSkuID().equals(oldS.getSkuID())));
            for (var sku : oldSkus) {
                if (!usageCheckService.isSkuIDInUse(sku.getSkuID())) {
                    skuMapper.deleteSku(sku.getSkuID());
                    logger.info("Deleted sku, id: {}", sku.getSkuID());
                    modificationRecordService.insertRecord(SKU, sku.getSkuID(),
                            usernameString + DELETION_MSG + sku);
                }
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateSkuBulk(int[] modelIDs, int[] brandIDs) {
        try {
            SkuO sku = new SkuO();
            for (var modelID : modelIDs) {
                sku.setModelID(modelID);
                List<SkuFullO> oldSkus = skuMapper.queryFullSkuByModel(modelID);

                for (var brandID : brandIDs) {
                    sku.setFactoryBrandID(brandID);
                    if (oldSkus.stream().noneMatch(s -> s.getFactoryBrandID() == brandID)) {
                        skuMapper.insertSku(sku);
                        logger.info("Inserted new sku, id: {}", sku.getSkuID());
                    }
                }
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<StockLimitO> getStockAlert() {
        try {
            var list = skuMapper.queryStockAlert();
            list.removeIf(s -> {
                if (s.getLowerLimit() != -1 && s.getStockQuantity() < s.getLowerLimit()) {
                    s.setDiff(s.getLowerLimit() - s.getStockQuantity());
                    return false;
                }
                if (s.getUpperLimit() != -1 && s.getStockQuantity() > s.getUpperLimit()) {
                    s.setDiff(s.getStockQuantity() - s.getUpperLimit());
                    return false;
                }
                s.setDiff(0);
                return true;
            });
            return list;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }
}
