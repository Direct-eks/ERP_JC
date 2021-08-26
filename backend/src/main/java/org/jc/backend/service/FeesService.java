package org.jc.backend.service;

import org.jc.backend.entity.FeeCategoryO;
import org.jc.backend.entity.VO.FeeEntryWithDetailVO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface FeesService {
    List<FeeCategoryO> getFeeCategories();
    void updateFeeCategories(List<FeeCategoryO> categories);

    void createEntry(FeeEntryWithDetailVO entryWithDetailVO, String prefix);
    void updateEntry(FeeEntryWithDetailVO entryWithDetailVO, boolean containsDetail);
}
