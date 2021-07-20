package org.jc.backend.service;

import org.jc.backend.entity.StatO.InboundSummaryO;
import org.jc.backend.entity.VO.PurchaseOrderEntryWithProductsVO;
import org.springframework.stereotype.Indexed;

import java.util.Date;
import java.util.List;

@Indexed
public interface PurchaseOrderService {
    void createOrder(PurchaseOrderEntryWithProductsVO entryWithProducts);
    List<PurchaseOrderEntryWithProductsVO> getOrdersInDateRangeByCompanyID(Date startDate, Date endDate, int id);
    List<PurchaseOrderEntryWithProductsVO> getOrdersByCompanyID(int id);
    void modifyOrder(PurchaseOrderEntryWithProductsVO modificationVO);
    void deleteOrder(String id);

    List<InboundSummaryO> getPurchaseSummary(Date startDate, Date endDate, int categoryID,
                                             String factoryBrand, int warehouseID, int departmentID);
}
