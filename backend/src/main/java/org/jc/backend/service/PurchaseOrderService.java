package org.jc.backend.service;

import org.jc.backend.config.exception.GlobalException;
import org.jc.backend.entity.VO.PurchaseOrderEntryWithProductsVO;
import org.jc.backend.entity.VO.PurchaseOrderModifyVO;

import java.util.Date;
import java.util.List;

public interface PurchaseOrderService {
    void createPurchaseOrder(PurchaseOrderEntryWithProductsVO entryWithProducts) throws GlobalException;
    List<PurchaseOrderEntryWithProductsVO> getOrdersWithinDateRangeByCompanyID(Date startDate, Date endDate, int id);
    void modifyPurchaseOrder(PurchaseOrderModifyVO modificationVO);
    void deletePurchaseOrder(String id);
}
