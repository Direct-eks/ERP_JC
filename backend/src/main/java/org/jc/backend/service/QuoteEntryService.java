package org.jc.backend.service;

import org.jc.backend.entity.VO.QuoteEntryWithProductsVO;
import org.springframework.stereotype.Indexed;

import java.util.Date;
import java.util.List;

@Indexed
public interface QuoteEntryService {
    void createOrder(QuoteEntryWithProductsVO quoteEntryWithProductsVO);
    List<QuoteEntryWithProductsVO> getOrdersInDateRangeByCompanyID(Date startDate, Date endDate, int id);
    List<QuoteEntryWithProductsVO> getOrdersByCompanyID(int id);
    void modifyOrder(QuoteEntryWithProductsVO quoteEntryWithProductsVO);
    void deleteOrder(String id);
}
