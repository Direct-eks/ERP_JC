package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.DO.CheckoutEntryDO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface CheckoutEntryMapper {
    int countNumberOfEntriesOfToday(String prefix);
    void insertEntry(CheckoutEntryDO checkoutEntryDO);

    List<CheckoutEntryDO> getEntriesInDateRangeByInvoiceTypeAndCompanyID(
            String startDate, String endDate, int companyID, String invoiceType,
            String prefix1, String prefix2);

    CheckoutEntryDO selectEntryBySerialForCompare(String serial);
    void modifyEntry(CheckoutEntryDO modifyDO);
    void returnEntry(CheckoutEntryDO returnDO);
}
