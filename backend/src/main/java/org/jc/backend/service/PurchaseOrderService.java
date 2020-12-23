package org.jc.backend.service;

import org.jc.backend.config.exception.GlobalException;
import org.jc.backend.entity.VO.PurchaseOrderEntryWithProductsVO;

import java.util.Date;
import java.util.List;

public interface PurchaseOrderService {
    void createNewPurchaseOrder(PurchaseOrderEntryWithProductsVO entryWithProducts) throws GlobalException;
    List<PurchaseOrderEntryWithProductsVO> getOrdersWithinDateRangeByCompanyID(Date startDate, Date endDate, int id);
}
