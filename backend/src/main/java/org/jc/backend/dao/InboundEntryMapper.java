package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.DO.InboundEntryDO;
import org.jc.backend.entity.DO.InboundEntryModifyDO;
import org.jc.backend.entity.InboundEntryCompleteO;
import org.jc.backend.entity.InboundProductModifyO;
import org.jc.backend.entity.InboundProductO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface InboundEntryMapper {

    int countNumberOfEntriesOfToday();
    void insertNewEntry(InboundEntryDO entryDO);
    int insertNewProduct(InboundProductO productDO);

    List<InboundEntryDO> queryEntriesInDateRangeByTypeAndCompanyID(String startDate, String endDate,
                                                                   String type, int id);
    List<InboundProductO> queryProductsByEntryID(String id);

    List<InboundEntryCompleteO> selectEntryShippingInfoForCompare(String id);
    void updateShippingInfo(InboundEntryCompleteO completionO);

    List<InboundEntryModifyDO> selectEntryForCompare(String id);
    List<InboundProductModifyO> selectProductsForCompare(String id);
    void updateEntry(InboundEntryModifyDO inboundEntryModifyDO);
    void updateProduct(InboundProductModifyO inboundProductModifyO);
    void deleteProductByID(int id);

    void deleteEntry(String id);
    void deleteProductsByEntryID(String id);

    List<String> queryEntriesByCompanyIDAndInvoiceType(int companyID, String invoiceType);

    void updateProductsWithCheckoutSerial(InboundProductO productO);
    List<InboundProductO> getProductsWithCheckoutSerial(String checkoutSerial);
}
