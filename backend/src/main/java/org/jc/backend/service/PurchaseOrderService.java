package org.jc.backend.service;

import org.jc.backend.config.exception.GlobalException;
import org.jc.backend.entity.VO.PurchaseOrderEntryWithProductsVO;
import org.jc.backend.entity.VO.PurchaseOrderModifyVO;

import java.util.Date;
import java.util.List;

public interface PurchaseOrderService {
    void createOrder(PurchaseOrderEntryWithProductsVO entryWithProducts);
    List<PurchaseOrderEntryWithProductsVO> getOrdersInDateRangeByCompanyID(Date startDate, Date endDate, int id);
    void modifyOrder(PurchaseOrderModifyVO modificationVO);
    void deleteOrder(String id);
}
