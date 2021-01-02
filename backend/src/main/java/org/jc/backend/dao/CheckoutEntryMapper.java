package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.DO.CheckoutEntryDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CheckoutEntryMapper {
    int countNumberOfEntriesOfToday(String prefix);
    void insertEntry(CheckoutEntryDO checkoutEntryDO);

    List<CheckoutEntryDO> getEntriesInDateRange(String startDate, String endDate,
                                                int companyID, String invoiceType);
}
