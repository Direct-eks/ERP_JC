package org.jc.backend.service;

import org.jc.backend.entity.VO.PurchaseOrderEntryWithProductsVO;

import java.util.Date;
import java.util.List;

public interface PurchaseOrderService {
    Boolean createNewPurchaseOrder(PurchaseOrderEntryWithProductsVO entryWithProducts);
    List<PurchaseOrderEntryWithProductsVO> getPurchaseOrdersWithinDateRangeByCompanyID(Date startDate, Date endDate, int id);
}
