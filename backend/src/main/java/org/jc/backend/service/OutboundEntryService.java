package org.jc.backend.service;

import org.jc.backend.config.exception.GlobalException;
import org.jc.backend.entity.DO.OutboundEntryDO;
import org.jc.backend.entity.OutboundProductO;
import org.jc.backend.entity.VO.OutboundEntryWithProductsVO;

import java.util.Date;
import java.util.List;

public interface OutboundEntryService {
    void createEntry(OutboundEntryWithProductsVO entryWithProductsVO) throws GlobalException;
    List<OutboundEntryWithProductsVO> getEntriesInDateRange(Date startDate, Date endDate,
                                                            String type, int id);
    void completeEntry(OutboundEntryWithProductsVO outboundEntryWithProductsVO);
    void modifyEntry(OutboundEntryWithProductsVO outboundEntryWithProductsVO);
    void deleteEntry(String id);
    void returnEntry(OutboundEntryWithProductsVO returnVO);

    List<OutboundProductO> getNotCheckedOutProducts(int companyID, String invoiceType);
    List<OutboundProductO> getCheckoutButNotInvoicedProducts(int companyID, String invoiceType);

    void updateProductsWithCheckoutSerial(List<OutboundProductO> products, String checkoutSerial);
    void updateProductsWithInvoiceSerial(List<OutboundProductO> products, String invoiceSerial);

    List<OutboundProductO> getProductsWithCheckoutSerial(String checkoutSerial);
    List<OutboundProductO> getProductsWithInvoiceSerial(String invoiceSerial);

    void updateEntryWithShippingCostSerial(OutboundEntryDO outboundEntryDO);
    List<OutboundEntryDO> getEntriesWithShippingCostSerial(String shippingCostSerial);
}
