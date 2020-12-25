package org.jc.backend.service;

import org.jc.backend.config.exception.GlobalException;
import org.jc.backend.entity.InboundEntryCompleteO;
import org.jc.backend.entity.VO.InboundEntryModifyVO;
import org.jc.backend.entity.VO.InboundEntryWithProductsVO;

import java.util.Date;
import java.util.List;

public interface InboundEntryService {
    void createEntry(InboundEntryWithProductsVO entryWithProductsVO) throws GlobalException;
    List<InboundEntryWithProductsVO> getEntriesInDateRangeByCompanyID(Date startDate, Date endDate, int id);
    void completeEntry(InboundEntryCompleteO completionVO);
    void modifyEntry(InboundEntryModifyVO modificationVO);
    void deleteEntry(String id);
}
