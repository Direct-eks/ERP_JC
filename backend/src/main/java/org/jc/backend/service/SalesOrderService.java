package org.jc.backend.service;

import org.jc.backend.entity.VO.SalesOrderEntryWithProductsVO;

import java.util.Date;
import java.util.List;

public interface SalesOrderService {
    void createOrder(SalesOrderEntryWithProductsVO salesOrderEntryWithProductsVO);
    List<SalesOrderEntryWithProductsVO> getOrdersInDateRangeByCompanyID(Date startDate, Date endDate, int id);
    List<SalesOrderEntryWithProductsVO> getOrdersByCompanyID(int id);
    void modifyOrder(SalesOrderEntryWithProductsVO salesOrderEntryWithProductsVO);
    void deleteOrder(String id);
}
