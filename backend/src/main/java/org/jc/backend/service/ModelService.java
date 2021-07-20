package org.jc.backend.service;

import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.ModelCategoryO;
import org.jc.backend.entity.ModelO;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.springframework.stereotype.Indexed;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Indexed
public interface ModelService {
    List<ModelCategoryO> getModelCategories();
    String getTreeLevelByCategoryID(int id);
    void updateModelCategories(ListUpdateVO<ModelCategoryO> updateVO);

    List<ModelO> getModelsByCategory(int id);
    List<ModelO> getModelsByName(String name, String method);

    void updateModelsWithCategory(int categoryID, int[] brands, ListUpdateVO<ModelO> updateVO);
    void updateCategoryOfModel(int modelID, int oldCategoryID, int newCategoryID) throws GlobalParamException;
    void exportAllModels(HttpServletResponse response);
}
