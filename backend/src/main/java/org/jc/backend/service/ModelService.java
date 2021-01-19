package org.jc.backend.service;

import org.jc.backend.entity.ModelCategoryO;
import org.jc.backend.entity.ModelO;

import java.util.List;

public interface ModelService {
    List<ModelCategoryO> getModelCategories();
    List<ModelO> getModelsByCategory(int id);
    List<ModelO> getModelsByName(String name, String category, String method);

    List<ModelO> getAllModels();
    List<ModelO> getModelsByCoarseCategoryID(int id);
}
