package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.DO.OutboundEntryDO;
import org.jc.backend.entity.OutboundProductO;
import org.jc.backend.entity.StatO.InvoiceStatDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OutboundEntryMapper {
    int countNumberOfEntriesOfToday();
    void insertNewEntry(OutboundEntryDO entryDO);
    int insertNewProduct(OutboundProductO productO);

    List<OutboundEntryDO> queryEntriesInDateRangeByTypeAndCompanyID(String startDate, String endDate,
                                                                    String type, int id);
    List<OutboundProductO> queryProductsByEntryID(String id);

    OutboundEntryDO selectEntryShippingInfoForCompare(String id);
    void updateShippingInfo(OutboundEntryDO completeO);

    OutboundEntryDO selectEntryForCompare(String id);
    List<OutboundProductO> selectProductsForCompare(String id);
    void updateEntry(OutboundEntryDO outboundEntryModifyDO);
    void updateProduct(OutboundProductO outboundProductModifyO);
    void deleteProductByID(int id);
    void returnProductByID(OutboundProductO outboundProductO);

    void deleteEntryByID(String id);
    void deleteProductsByEntryID(String id);

    List<String> queryEntriesByCompanyIDAndInvoiceType(int companyID, String invoiceType);

    void updateProductsWithCheckoutSerial(OutboundProductO productO);
    void updateProductsWithInvoiceSerial(OutboundProductO productO);

    List<OutboundProductO> getProductsWithCheckoutSerial(String checkoutSerial);
    List<OutboundProductO> getProductsWithInvoiceSerial(String invoiceSerial);

    void updateEntryWithShippingCostSerial(OutboundEntryDO outboundEntryDO);
    List<OutboundEntryDO> getEntriesWithShippingCostSerial(String shippingCostSerial);

    List<OutboundEntryDO> getEntriesByCompanyAndShippingCostType(
            int companyID, String shippingCostType);

    List<InvoiceStatDO> queryNotYetCheckoutSummary();
    List<OutboundProductO> queryNotYetCheckoutDetailByCompanyID(int id);
    List<InvoiceStatDO> queryNotYetInvoiceSummary();
    List<OutboundProductO> queryNotYetInvoiceDetailByCompanyID(int id);

    List<OutboundProductO> queryProductsByWarehouseStockID(int id);
}
