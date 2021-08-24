package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.dao.ModelMapper;
import org.jc.backend.entity.ModelCategoryO;
import org.jc.backend.entity.ModelO;
import org.jc.backend.service.ModelService;
import org.jc.backend.service.ModificationRecordService;
import org.jc.backend.service.SkuService;
import org.jc.backend.service.UsageCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.jc.backend.service.ModificationRecordService.*;

@Service
public class ModelServiceImpl implements ModelService {

    private static final Logger logger = LoggerFactory.getLogger(ModelServiceImpl.class);

    private final ModelMapper modelMapper;
    private final SkuService skuService;
    private final ModificationRecordService modificationRecordService;
    private final UsageCheckService usageCheckService;

    public ModelServiceImpl(
            ModelMapper modelMapper,
            @Lazy SkuService skuService,
            ModificationRecordService modificationRecordService,
            UsageCheckService usageCheckService
    ) {
        this.modelMapper = modelMapper;
        this.skuService = skuService;
        this.modificationRecordService = modificationRecordService;
        this.usageCheckService = usageCheckService;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional(readOnly = true)
    public List<ModelCategoryO> getModelCategories() {
        try {
            return modelMapper.queryModelCategories();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query error");
            throw e;
        }
    }

    @Override
    public String getTreeLevelByCategoryID(int categoryID) {
        var categories = modelMapper.queryModelCategories();
        String treeLevel = null;
        for (var c : categories) {
            if (c.getModelCategoryID() == categoryID) {
                treeLevel = c.getTreeLevel();
                break;
            }
        }
        return treeLevel == null ? "" : treeLevel;
    }

    /**
     * get models by category id. Since models only use the tail node in category
     * tree, here the id must be a tail node id.
     */
    @Transactional(readOnly = true)
    public List<ModelO> getModelsByCategory(int id) {
        try {
            return modelMapper.queryModelsByCategory(id);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query error");
            throw e;
        }
    }

    @Override
    public void updateModelCategories(List<ModelCategoryO> updateVO) {
        Subject subject = SecurityUtils.getSubject();
        String usernameString = "修改者：" + subject.getPrincipals().getPrimaryPrincipal() + "; ";

        try {
            List<ModelCategoryO> tempCategories = new ArrayList<>(updateVO);
            List<ModelCategoryO> oldCategories = modelMapper.queryModelCategories();

            tempCategories.removeIf(c -> c.getModelCategoryID() >= 0);
            for (var category : tempCategories) {
                modelMapper.insertModelCategory(category);
                logger.info("Inserted new model category, id: {}", category.getModelCategoryID());
            }

            tempCategories = new ArrayList<>(updateVO);
            tempCategories.removeIf(c -> c.getModelCategoryID() < 0);
            for (var category : tempCategories) {
                StringBuilder record = new StringBuilder(usernameString);
                if (category.formModificationRecord(category.getOldObject(oldCategories), record)) {
                    modelMapper.updateModelCategory(category);
                    logger.info("Updated model category, id: {}", category.getModelCategoryID());
                    modificationRecordService.insertRecord(MODEL_CATEGORY, category.getModelCategoryID(), record);
                }
            }

            // check for removed
            oldCategories.removeIf(oldC -> updateVO.stream()
                .anyMatch(c -> c.getModelCategoryID().equals(oldC.getModelCategoryID())));
            for (var category : oldCategories) {
                if (!usageCheckService.isModelCategoryIDInUse(category.getModelCategoryID())) {
                    modelMapper.deleteModelCategory(category.getModelCategoryID());
                    logger.info("Updated model category, id: {}", category.getModelCategoryID());
                    modificationRecordService.insertRecord(MODEL_CATEGORY, category.getModelCategoryID(),
                            usernameString + DELETION_MSG + category);
                }
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update error");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ModelO> getModelsByName(String name, String method) {
        try {
            return modelMapper.queryModelsByName(name, method);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query error");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateModelsWithCategory(int categoryID, int[] brands, List<ModelO> updateVO) {
        Subject subject = SecurityUtils.getSubject();
        String usernameString = "修改者：" + subject.getPrincipals().getPrimaryPrincipal() + "; ";

        try {
            List<ModelO> tempModels = new ArrayList<>(updateVO);
            List<ModelO> oldModels = modelMapper.queryModelsByCategory(categoryID);

            // check for added
            if (subject.isPermitted("system:models:create")) {
                tempModels.removeIf(m -> m.getModelID() >= 0);
                for (var model : tempModels) {
                    modelMapper.insertModel(model);
                    logger.info("Inserted new model, id: {}", model.getModelID());
                }
                // now modelIDs are created, create sku based on the brands
                if (brands.length > 0 && tempModels.size() > 0)
                    skuService.createNewSkus(tempModels, brands);
            }

            // update all
            if (subject.isPermitted("system:models:update")) {
                tempModels = new ArrayList<>(updateVO);
                tempModels.removeIf(i -> i.getModelID() < 0);
                for (var model : tempModels) {
                    StringBuilder record = new StringBuilder(usernameString);
                    if (model.formModificationRecord(model.getOldObject(oldModels), record)) {
                        modelMapper.updateModel(model);
                        logger.info("Updated model, id: {}", model.getModelID());
                        modificationRecordService.insertRecord(MODEL, model.getModelID(), record);
                    }
                }
            }

            // check for removed
            if (subject.isPermitted("system:models:remove")) {
                oldModels.removeIf(oldM -> updateVO.stream()
                        .anyMatch(m -> m.getModelID().equals(oldM.getModelID())));
                for (var model : oldModels) {
                    if (!usageCheckService.isModelIDInUse(model.getModelID())) {
                        modelMapper.deleteModel(model.getModelID());
                        logger.info("Deleted model, id: {}", model.getModelID());
                        modificationRecordService.insertRecord(MODEL, model.getModelID(),
                                usernameString + DELETION_MSG + model);
                    }
                }
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update error");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateCategoryOfModel(int modelID, int oldCategoryID, int newCategoryID) throws GlobalParamException {
        Subject subject = SecurityUtils.getSubject();
        String usernameString = "修改者：" + subject.getPrincipals().getPrimaryPrincipal() + "; ";

        try {
            if (subject.isPermitted("system:models:update")) {
                var modelsFromOldCategory = modelMapper.queryModelsByCategory(oldCategoryID);
                ModelO modelToBeUpdated = null;
                for (var model : modelsFromOldCategory) {
                    if (model.getModelID().equals(modelID)) {
                        modelToBeUpdated = model;
                        break;
                    }
                }
                if (modelToBeUpdated == null) {
                    throw new GlobalParamException("oldModelCategory does not contain modelID");
                }
                var modelsFromNewCategory = modelMapper.queryModelsByCategory(newCategoryID);
                for (var model : modelsFromNewCategory) {
                    if (model.getCode().equals(modelToBeUpdated.getCode())) {
                        throw new GlobalParamException("代号存在重复!");
                    }
                }

                modelMapper.updateCategoryOfModel(modelID, newCategoryID);
                logger.info("Updated model, id: {}", modelID);
                modificationRecordService.insertRecord(MODEL, modelID,
                        String.format("%s分类：%d -> %d", usernameString, oldCategoryID, newCategoryID));
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update error");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public void exportAllModels(HttpServletResponse response) {
        try {
            List<ModelO> allModels = modelMapper.queryAllModels();

            SXSSFWorkbook workbook = new SXSSFWorkbook();
            Sheet sheet = workbook.createSheet("所有型号");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("所在分类");
            header.createCell(1).setCellValue("子类序号");
            header.createCell(2).setCellValue("型号");
            header.createCell(3).setCellValue("计量单位");

            int rowIndex = 1;
            for (var model : allModels) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(model.getCategoryCode());
                row.createCell(1).setCellValue(model.getSequenceNumber());
                row.createCell(2).setCellValue(model.getCode());
                row.createCell(3).setCellValue(model.getUnitName());
            }

            response.reset();
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" +
                            URLEncoder.encode("型号表.xlsx", StandardCharsets.UTF_8));

            workbook.write(response.getOutputStream());
            workbook.dispose();
            workbook.close();
            response.flushBuffer();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query error");
            throw e;
        } catch (IOException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("io failed");
        }
    }
}
