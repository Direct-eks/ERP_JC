package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.jc.backend.dao.ModelMapper;
import org.jc.backend.entity.ModelCategoryO;
import org.jc.backend.entity.ModelO;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.jc.backend.service.ModelService;
import org.jc.backend.service.SkuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ModelServiceImpl implements ModelService {

    private static final Logger logger = LoggerFactory.getLogger(ModelServiceImpl.class);

    private final ModelMapper modelMapper;
    private final SkuService skuService;

    public ModelServiceImpl(
            ModelMapper modelMapper,
            @Lazy SkuService skuService
    ) {
        this.modelMapper = modelMapper;
        this.skuService = skuService;
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
    public void updateModelsWithCategory(int categoryID, int[] brands, ListUpdateVO<ModelO> updateVO) {
        try {
            List<ModelO> tempModels = new ArrayList<>(updateVO.getElements());

            // check for added
            tempModels.removeIf(m -> m.getModelID() >= 0);
            for (var model : tempModels) {
                modelMapper.insertModel(model);
            }
            // now modelIDs are created, create sku based on the brands
            if (brands.length > 0 && tempModels.size() > 0)
                skuService.createNewSkus(tempModels, brands);

            // update all
            tempModels = new ArrayList<>(updateVO.getElements());
            tempModels.removeIf(i -> i.getModelID() < 0);
            for (var model : tempModels) {
                modelMapper.updateModel(model);
            }

            // todo remove
            List<ModelO> oldModels = modelMapper.queryModelsByCategory(categoryID);

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

            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment;filename=型号表.xlsx");

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
