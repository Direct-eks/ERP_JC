package org.jc.backend.service;

import org.jc.backend.entity.InboundEntryCompleteO;
import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.VO.InboundEntryModifyVO;
import org.jc.backend.entity.VO.InboundEntryWithProductsVO;

import java.util.Date;
import java.util.List;

public interface InboundEntryService {
    void createEntry(InboundEntryWithProductsVO entryWithProductsVO);
    List<InboundEntryWithProductsVO> getEntriesInDateRangeByTypeAndCompanyID(Date startDate, Date endDate,
                                                                             String type, int companyID);
    void completeEntry(InboundEntryCompleteO completionVO);
    void modifyEntry(InboundEntryModifyVO modificationVO);
    void deleteEntry(String id);

    List<InboundProductO> getProductsByCompanyIDAndInvoiceType(int companyID, String invoiceType);

    void updateProductsWithCheckoutSerial(List<InboundProductO> products, String checkoutSerial);
}
