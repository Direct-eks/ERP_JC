package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.ModelMapper;
import org.jc.backend.entity.ModelCategoryO;
import org.jc.backend.entity.ModelO;
import org.jc.backend.service.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModelServiceImpl implements ModelService {

    private static final Logger logger = LoggerFactory.getLogger(ModelServiceImpl.class);

    private final ModelMapper modelMapper;

    public ModelServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
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

    @Transactional(readOnly = true)
    public List<ModelO> getAllModels() {
        try {
            return modelMapper.queryAllModels();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query error");
            throw e;
        }
    }

    /**
     * get modesl by coarse category, all models lie below the sub model category tree
     * will be retrieved
     */
    @Transactional(readOnly = true)
    public List<ModelO> getModelsByCoarseCategoryID(int id) {
        try {
            List<ModelCategoryO> allCategories = modelMapper.queryModelCategories();
            String treeLevel = null;
            for (var category : allCategories) {
                if (category.getModelCategoryID() == id) {
                    treeLevel = category.getTreeLevel();
                    break;
                }
            }

            return modelMapper.queryModelsByCoarseCategory(treeLevel);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query error");
            throw e;
        }
    }
}
