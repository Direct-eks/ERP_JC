package org.jc.backend.service;

import org.jc.backend.entity.DO.PurchaseOrderEntryDO;
import org.jc.backend.entity.VO.PurchaseOrderEntryWithProductsVO;

import java.util.Date;
import java.util.List;

public interface PurchaseOrderService {
    void createOrder(PurchaseOrderEntryWithProductsVO entryWithProducts);
    List<PurchaseOrderEntryWithProductsVO> getOrdersInDateRangeByCompanyID(
            Date startDate, Date endDate, int id);
    void modifyOrder(PurchaseOrderEntryWithProductsVO modificationVO);
    void deleteOrder(String id);
}
