package org.jc.backend.service;

import org.jc.backend.entity.ModelCategoryO;
import org.jc.backend.entity.ModelO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ModelService {
    List<ModelCategoryO> getModelCategories();
    List<ModelO> getModelsByCategory(int id);
    List<ModelO> getModelsByName(String name, String method);

    void exportAllModels(HttpServletResponse response);
}
