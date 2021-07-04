package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.ModificationMapper;
import org.jc.backend.dao.QuoteEntryMapper;
import org.jc.backend.entity.DO.QuotaEntryDO;
import org.jc.backend.entity.ModificationO;
import org.jc.backend.entity.QuoteProductO;
import org.jc.backend.entity.VO.QuoteEntryWithProductsVO;
import org.jc.backend.service.QuoteEntryService;
import org.jc.backend.utils.IOModificationUtils;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class QuoteEntryServiceImpl implements QuoteEntryService {

    private static final Logger logger = LoggerFactory.getLogger(QuoteEntryServiceImpl.class);

    private final QuoteEntryMapper quoteEntryMapper;
    private final ModificationMapper modificationMapper;

    public QuoteEntryServiceImpl(QuoteEntryMapper quoteEntryMapper,
                                 ModificationMapper modificationMapper) {
        this.quoteEntryMapper = quoteEntryMapper;
        this.modificationMapper = modificationMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    @Override
    public void createQuote(QuoteEntryWithProductsVO quoteEntryWithProductsVO) {

        QuotaEntryDO newEntry = new QuotaEntryDO();
        BeanUtils.copyProperties(quoteEntryWithProductsVO, newEntry);
        List<QuoteProductO> newProducts = quoteEntryWithProductsVO.getQuoteProducts();

        try {
            // calculate the number of entries have been created for today's date, and generate new serial
            int count = quoteEntryMapper.countNumberOfEntriesOfToday();
            String newSerial = MyUtils.formNewSerial("报价", count, newEntry.getCreationDate());

            newEntry.setQuotaEntryID(newSerial);
            quoteEntryMapper.insertNewOrderEntry(newEntry);

            for (var product : newProducts) {
                product.setQuotaEntryID(newSerial);
                quoteEntryMapper.insertNewOrderProduct(product);
                int id = product.getQuotaProductID();
                logger.info("Insert new purchase product id: " + id);
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Insert failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<QuoteEntryWithProductsVO> getQuotesInDateRangeByCompanyID(Date startDate, Date endDate, int id) {
        try {
            List<QuotaEntryDO> entriesFromDatabase = quoteEntryMapper.queryEntriesInDateRangeByCompanyID(
                    MyUtils.dateFormat.format(startDate), MyUtils.dateFormat.format(endDate), id);

            List<QuoteEntryWithProductsVO> entries = new ArrayList<>();
            for (var entryFromDatabase : entriesFromDatabase) {
                QuoteEntryWithProductsVO tempEntry = new QuoteEntryWithProductsVO();
                BeanUtils.copyProperties(entryFromDatabase, tempEntry);

                List<QuoteProductO> products = quoteEntryMapper.queryProductsByEntryID(
                        tempEntry.getQuoteEntryID());
                tempEntry.setQuoteProducts(products);

                entries.add(tempEntry);
            }
            return entries;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query error");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<QuoteEntryWithProductsVO> getQuotesByCompanyID(int id) {
        try {
            List<QuotaEntryDO> entriesFromDatabase = quoteEntryMapper.queryEntriesByCompanyID(id);

            List<QuoteEntryWithProductsVO> entries = new ArrayList<>();
            for (var entryFromDatabase : entriesFromDatabase) {
                QuoteEntryWithProductsVO tempEntry = new QuoteEntryWithProductsVO();
                BeanUtils.copyProperties(entryFromDatabase, tempEntry);

                List<QuoteProductO> products = quoteEntryMapper.queryProductsByEntryID(
                        tempEntry.getQuoteEntryID());
                tempEntry.setQuoteProducts(products);

                entries.add(tempEntry);
            }
            return entries;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query error");
            throw e;
        }
    }

    @Transactional
    @Override
    public void modifyQuote(QuoteEntryWithProductsVO quoteEntryWithProductsVO) {

        QuotaEntryDO currentEntry = new QuotaEntryDO();
        BeanUtils.copyProperties(quoteEntryWithProductsVO, currentEntry);

        List<QuoteProductO> currentProducts = quoteEntryWithProductsVO.getQuoteProducts();

        try {
            //query database for compare
            String id = currentEntry.getQuotaEntryID();
            logger.info("Serial to be changed: " + id);

            QuotaEntryDO originEntry = quoteEntryMapper.selectEntryForCompare(id);
            List<QuoteProductO> originProducts = quoteEntryMapper.selectProductsForCompare(id);

            //compare entry
            StringBuilder record = new StringBuilder("修改者: " + currentEntry.getDrawer() + "; "); //modification_record
            boolean bool1 = IOModificationUtils.entryCompareAndFormModificationRecord(record, currentEntry, originEntry);

            if (bool1) {
                quoteEntryMapper.updateOrderEntry(currentEntry);
            }

            boolean bool2 = false; //bool to indicate changes to products
            for (var originProduct : originProducts) {
                boolean found = false;
                for (var currentProduct : currentProducts) {
                    if (currentProduct.getQuotaProductID() == originProduct.getQuotaProductID()) {
                        boolean bool3 = IOModificationUtils.productCompareAndFormModificationRecord(
                                record, currentProduct, originProduct);

                        if (bool3) {
                            bool2 = true; //bool2 here in case only one product is changed and bool2 will be overwritten
                            quoteEntryMapper.updateOrderProduct(currentProduct);
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) { //entry is removed
                    quoteEntryMapper.deleteOrderProductByID(originProduct.getQuotaProductID());
                }
            }

            if (bool1 || bool2) {
                logger.info("Modification: " + record);
                modificationMapper.insertModificationRecord(new ModificationO(
                        originEntry.getQuotaEntryID(), record.toString()));
            }
            else {
                logger.warn("Nothing changed, begin rolling back");
                throw new RuntimeException();
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Update error");
            throw e;
        }
    }

    @Transactional
    @Override
    public void deleteQuote(String id) {
        try {
            quoteEntryMapper.deleteOrderProductsByEntryID(id);
            quoteEntryMapper.deleteOrderEntry(id);
        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Delete error");
            throw e;
        }
    }

}
