package org.jc.backend.service;

import org.jc.backend.entity.VO.QuotaEntryWithProductsVO;

import java.util.Date;
import java.util.List;

public interface QuotaEntryService {
    void createOrder(QuotaEntryWithProductsVO quotaEntryWithProductsVO);
    List<QuotaEntryWithProductsVO> getOrdersInDateRangeByCompanyID(Date startDate, Date endDate, int id);
    List<QuotaEntryWithProductsVO> getOrdersByCompanyID(int id);
    void modifyOrder(QuotaEntryWithProductsVO quotaEntryWithProductsVO);
    void deleteOrder(String id);
}
