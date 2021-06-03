package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.InvoiceEntryO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface InvoiceEntryMapper {
    int countNumberOfEntriesOfToday(String prefix);
    void insertEntry(InvoiceEntryO invoiceEntryO);
    List<InvoiceEntryO> getEntriesInDateRangeAndParams(String startDate, String endDate, String invoiceNumberDate,
                                                       int companyID, int isFollowUpIndication,
                                                       String invoiceNumber, String invoiceType,
                                                       String prefix1, String prefix2);
    InvoiceEntryO selectEntryBySerialForCompare(String serial);
    void updateEntry(InvoiceEntryO invoiceEntryO);
}
