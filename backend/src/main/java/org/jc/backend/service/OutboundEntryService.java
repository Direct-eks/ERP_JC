package org.jc.backend.service;

import org.jc.backend.config.exception.GlobalException;
import org.jc.backend.entity.OutboundEntryCompleteO;
import org.jc.backend.entity.VO.OutboundEntryModifyVO;
import org.jc.backend.entity.VO.OutboundEntryWithProductsVO;

import java.util.Date;
import java.util.List;

public interface OutboundEntryService {
    void createEntry(OutboundEntryWithProductsVO entryWithProductsVO) throws GlobalException;
    List<OutboundEntryWithProductsVO> getEntriesInDateRangeByCompanyID(Date startDate, Date endDate, int id);
    void completeEntry(OutboundEntryCompleteO completeO);
    void modifyEntry(OutboundEntryModifyVO modifyVO);
    void deleteEntry(String id);
}
