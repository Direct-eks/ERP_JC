package org.jc.backend.service.Impl;

import org.jc.backend.dao.ModelMapper;
import org.jc.backend.entity.ModelCategoryO;
import org.jc.backend.entity.ModelO;
import org.jc.backend.service.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
public class ModelServiceImpl implements ModelService {

    private static final Logger logger = LoggerFactory.getLogger(ModelServiceImpl.class);

    private final ModelMapper modelMapper;

    public ModelServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @GetMapping()
    public List<ModelCategoryO> getModelCategories() {
        return modelMapper.queryModelCategories();
    }

    @PostMapping()
    public List<ModelO> getModelsByCategory(int id) {
        return modelMapper.queryModelsByCategory(id);
    }
}
