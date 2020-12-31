package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.InvoiceEntryO;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface InvoiceEntryMapper {
    int countNumberOfEntriesOfToday(String prefix);
    void insertEntry(InvoiceEntryO invoiceEntryO);
}
