package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.DO.InboundEntryDO;
import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.StatO.InvoiceStatVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface InboundEntryMapper {

    int countNumberOfEntriesOfToday();
    void insertNewEntry(InboundEntryDO entryDO);
    int insertNewProduct(InboundProductO productDO);

    List<InboundEntryDO> queryEntriesInDateRangeByInvoiceTypeAndCompanyID(
            String startDate, String endDate, String type, int id);
    List<InboundProductO> queryProductsByEntryID(String id);

    InboundEntryDO selectEntryShippingInfoForCompare(String id);
    void updateShippingInfo(InboundEntryDO completionO);

    InboundEntryDO selectEntryForCompare(String id);
    List<InboundProductO> selectProductsForCompare(String id);
    void updateEntry(InboundEntryDO inboundEntryModifyDO);
    void updateProduct(InboundProductO inboundProductModifyO);
    void deleteProductByID(int id);
    void returnProductByID(InboundProductO returnProductO);

    void deleteEntryByID(String id);
    void deleteProductsByEntryID(String id);

    List<String> queryEntriesByCompanyIDAndInvoiceType(int companyID, String invoiceType);

    void updateProductsWithCheckoutSerial(InboundProductO productO);
    void updateProductsWithInvoiceSerial(InboundProductO productO);

    List<InboundProductO> getProductsWithCheckoutSerial(String checkoutSerial);
    List<InboundProductO> getProductsWithInvoiceSerial(String invoiceSerial);

    void updateEntryWithShippingCostSerial(InboundEntryDO inboundEntryDO);
    List<InboundEntryDO> getEntriesWithShippingCostSerial(String shippingCostSerial);

    List<InboundEntryDO> getEntriesByCompanyAndShippingCostType(
            int companyID, String shippingCostType);

    List<InvoiceStatVO> queryNotYetCheckoutSummary();
    List<InboundProductO> queryNotYetCheckoutDetailByCompanyID(int id);
    List<InvoiceStatVO> queryNotYetInvoiceSummary();
    List<InboundProductO> queryNotYetInvoiceDetailByCompanyID(int id);

    List<InboundProductO> queryProductsByWarehouseStockID(int id);
}
