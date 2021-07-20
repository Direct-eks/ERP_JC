package org.jc.backend.service;

import org.jc.backend.entity.StatO.SummaryO;
import org.jc.backend.entity.VO.SalesOrderEntryWithProductsVO;
import org.springframework.stereotype.Indexed;

import java.util.Date;
import java.util.List;

@Indexed
public interface SalesOrderService {
    void createOrder(SalesOrderEntryWithProductsVO salesOrderEntryWithProductsVO);
    List<SalesOrderEntryWithProductsVO> getOrdersInDateRangeByCompanyID(Date startDate, Date endDate, int id);
    List<SalesOrderEntryWithProductsVO> getOrdersByCompanyID(int id);
    void modifyOrder(SalesOrderEntryWithProductsVO salesOrderEntryWithProductsVO);
    void deleteOrder(String id);

    List<SummaryO> getSalesSummary(Date startDate, Date endDate, int categoryID,
                                   String factoryBrand, int warehouseID, int departmentID);
}
