package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.DO.CheckoutEntryDO;
import org.jc.backend.entity.StatO.CheckoutSummaryO;
import org.jc.backend.entity.VO.CheckoutEntryWithProductsVO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface CheckoutEntryMapper {
    int countNumberOfEntriesOfToday(String date, String prefix);
    void insertEntry(CheckoutEntryDO checkoutEntryDO);
    void updateEntryWithInvoice(String serial, String invoiceSerial);

    List<CheckoutEntryDO> getEntriesInDateRangeByInvoiceTypeAndCompanyID(
            String startDate, String endDate, int companyID, String invoiceType, String prefix);

    CheckoutEntryDO selectEntryBySerialForCompare(String serial);
    void modifyEntry(CheckoutEntryDO modifyDO);
    void returnEntry(CheckoutEntryWithProductsVO returnVO);

    List<CheckoutSummaryO> getInboundSummary(String startDate, String endDate, int companyID, String treeLevel,
                                             String factoryBrand, int warehouseID, int departmentID);
    List<CheckoutSummaryO> getOutboundSummary(String startDate, String endDate, int companyID, String treeLevel,
                                              String factoryBrand, int warehouseID, int departmentID);

    void updateVerifiedEntry(String serial, int value);

    List<CheckoutEntryDO> queryAllEntriesByPrefixAndCompany(String prefix, int id);
}
