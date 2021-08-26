package org.jc.backend.service;

import org.jc.backend.entity.FeeCategoryO;
import org.jc.backend.entity.VO.FeeEntryWithDetailVO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface FeesService {
    List<FeeCategoryO> getFeeCategories();
    void updateFeeCategories(List<FeeCategoryO> categories);

    void createEntry(FeeEntryWithDetailVO entryWithDetailVO, String prefix, boolean hasDetails);
    List<FeeEntryWithDetailVO> getEntriesInDateRange(String startDate, String endDate,
                                                     String prefix, boolean hasDetails);
    void updateEntry(FeeEntryWithDetailVO entryWithDetailVO, boolean containsDetail);
}
