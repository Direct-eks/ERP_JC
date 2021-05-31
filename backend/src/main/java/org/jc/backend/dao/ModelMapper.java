package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.ModelCategoryO;
import org.jc.backend.entity.ModelO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ModelMapper {
    List<ModelCategoryO> queryModelCategories();
    List<ModelO> queryModelsByCategory(int id);
    List<ModelO> queryModelsByCoarseCategory(String treeLevel);
    List<ModelO> queryModelsByName(String name, String method);

    List<ModelO> queryAllModels();
}
