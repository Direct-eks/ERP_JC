package org.jc.backend.service;

import org.jc.backend.entity.FeeCategoryO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface FeesService {
    List<FeeCategoryO> getFeeCategories();
    void updateFeeCategories(List<FeeCategoryO> categories);
}
