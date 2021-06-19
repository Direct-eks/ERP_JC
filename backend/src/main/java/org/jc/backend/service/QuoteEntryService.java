package org.jc.backend.service;

import org.jc.backend.entity.VO.QuoteEntryWithProductsVO;
import org.springframework.stereotype.Indexed;

import java.util.Date;
import java.util.List;

@Indexed
public interface QuoteEntryService {
    void createQuote(QuoteEntryWithProductsVO quoteEntryWithProductsVO);
    List<QuoteEntryWithProductsVO> getQuotesInDateRangeByCompanyID(Date startDate, Date endDate, int id);
    List<QuoteEntryWithProductsVO> getQuotesByCompanyID(int id);
    void modifyQuote(QuoteEntryWithProductsVO quoteEntryWithProductsVO);
    void deleteQuote(String id);
}
