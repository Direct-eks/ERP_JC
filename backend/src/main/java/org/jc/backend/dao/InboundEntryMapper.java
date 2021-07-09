package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.DO.InboundEntryDO;
import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.StatO.InboundSummaryO;
import org.jc.backend.entity.StatO.InvoiceStatDO;
import org.jc.backend.entity.StatO.ProductStatO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface InboundEntryMapper {

    int countNumberOfEntriesOfGivenDate(String date);
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
    void updateEntryReturn(InboundProductO inboundProductO);
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

    List<InvoiceStatDO> queryNotYetCheckoutSummary();
    List<InboundProductO> queryNotYetCheckoutDetailByCompanyID(int id);
    List<InvoiceStatDO> queryNotYetInvoiceSummary();
    List<InboundProductO> queryNotYetInvoiceDetailByCompanyID(int id);

    List<InboundProductO> queryProductsByWarehouseStockID(int id);

    // for stock price calculation
    List<ProductStatO> queryAllInboundProductsByWarehouseStockID(int id);
    void updateProductStockInfo(ProductStatO productStatO);

    List<InboundSummaryO> queryInboundSummary(String treeLevel);
}
