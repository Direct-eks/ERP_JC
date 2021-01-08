package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.ModificationMapper;
import org.jc.backend.dao.QuotaEntryMapper;
import org.jc.backend.entity.DO.PurchaseOrderEntryDO;
import org.jc.backend.entity.DO.QuotaEntryDO;
import org.jc.backend.entity.ModificationO;
import org.jc.backend.entity.PurchaseOrderProductO;
import org.jc.backend.entity.QuotaProductO;
import org.jc.backend.entity.VO.PurchaseOrderEntryWithProductsVO;
import org.jc.backend.entity.VO.QuotaEntryWithProductsVO;
import org.jc.backend.service.QuotaEntryService;
import org.jc.backend.utils.IOModificationUtils;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class QuotaEntryServiceImpl implements QuotaEntryService {

    private static final Logger logger = LoggerFactory.getLogger(QuotaEntryServiceImpl.class);

    private final QuotaEntryMapper quotaEntryMapper;
    private final ModificationMapper modificationMapper;

    public QuotaEntryServiceImpl(QuotaEntryMapper quotaEntryMapper,
                                 ModificationMapper modificationMapper) {
        this.quotaEntryMapper = quotaEntryMapper;
        this.modificationMapper = modificationMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    public void createOrder(QuotaEntryWithProductsVO quotaEntryWithProductsVO) {

        QuotaEntryDO newEntry = new QuotaEntryDO();
        BeanUtils.copyProperties(quotaEntryWithProductsVO, newEntry);
        List<QuotaProductO> newProducts = quotaEntryWithProductsVO.getQuotaProducts();

        try {
            // calculate the number of entries have been created for today's date, and generate new serial
            int count = quotaEntryMapper.countNumberOfEntriesOfToday();
            String newSerial = MyUtils.formNewSerial("报价", count, newEntry.getCreationDate());

            newEntry.setQuotaEntryID(newSerial);
            quotaEntryMapper.insertNewOrderEntry(newEntry);

            for (var product : newProducts) {
                product.setQuotaEntryID(newSerial);
                int id = quotaEntryMapper.insertNewOrderProduct(product);
                logger.info("Insert new purchase product id: " + id);
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production mode
            logger.error("Insert failed");
            throw e;
        }

    }

    @Transactional(readOnly = true)
    public List<QuotaEntryWithProductsVO> getOrdersInDateRangeByCompanyID(Date startDate, Date endDate, int id) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<QuotaEntryWithProductsVO> entries = new ArrayList<>();
        try {
            List<QuotaEntryDO> entriesFromDatabase = quotaEntryMapper.queryEntriesInDateRangeByCompanyID(
                    dateFormat.format(startDate), dateFormat.format(endDate), id);

            for (var entryFromDatabase : entriesFromDatabase) {
                QuotaEntryWithProductsVO tempEntry = new QuotaEntryWithProductsVO();
                BeanUtils.copyProperties(entryFromDatabase, tempEntry);

                List<QuotaProductO> products = quotaEntryMapper.queryProductsByEntryID(
                        tempEntry.getQuotaEntryID());
                tempEntry.setQuotaProducts(products);

                entries.add(tempEntry);
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production mode
            logger.error("Query error");
            throw e;
        }

        return entries;
    }

    @Transactional(readOnly = true)
    public List<QuotaEntryWithProductsVO> getOrdersByCompanyID(int id) {

        List<QuotaEntryWithProductsVO> entries = new ArrayList<>();
        try {
            List<QuotaEntryDO> entriesFromDatabase = quotaEntryMapper.queryEntriesByCompanyID(id);

            for (var entryFromDatabase : entriesFromDatabase) {
                QuotaEntryWithProductsVO tempEntry = new QuotaEntryWithProductsVO();
                BeanUtils.copyProperties(entryFromDatabase, tempEntry);

                List<QuotaProductO> products = quotaEntryMapper.queryProductsByEntryID(
                        tempEntry.getQuotaEntryID());
                tempEntry.setQuotaProducts(products);

                entries.add(tempEntry);
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production mode
            logger.error("Query error");
            throw e;
        }

        return entries;
    }

    @Transactional
    public void modifyOrder(QuotaEntryWithProductsVO quotaEntryWithProductsVO) {

        QuotaEntryDO currentEntry = new QuotaEntryDO();
        BeanUtils.copyProperties(quotaEntryWithProductsVO, currentEntry);

        List<QuotaProductO> currentProducts = quotaEntryWithProductsVO.getQuotaProducts();

        //query database for compare
        String id = currentEntry.getQuotaEntryID();
        logger.info("Serial to be changed: " + id);
        QuotaEntryDO originEntry;
        List<QuotaProductO> originProducts;
        try {
            originEntry = quotaEntryMapper.selectEntryForCompare(id);
            originProducts = quotaEntryMapper.selectProductsForCompare(id);

            //compare entry
            StringBuilder record = new StringBuilder("修改者: " + currentEntry.getDrawer() + "; "); //modification_record
            boolean bool1 = IOModificationUtils.entryCompareAndFormModificationRecord(record, currentEntry, originEntry);

            if (bool1) {
                quotaEntryMapper.updateOrderEntry(currentEntry);
            }

            boolean bool2 = false; //bool to indicate changes to products
            for (var originProduct : originProducts) {
                boolean found = false;
                for (var currentProduct : currentProducts) {
                    if (currentProduct.getQuotaProductID() == originProduct.getQuotaProductID()) {
                        boolean bool3 = IOModificationUtils.productsCompareAndFormModificationRecord(
                                record, currentProduct, originProduct);

                        if (bool3) {
                            bool2 = true; //bool2 here in case only one product is changed and bool2 will be overwritten
                            quotaEntryMapper.updateOrderProduct(currentProduct);
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) { //entry is removed
                    quotaEntryMapper.deleteOrderProductByID(originProduct.getQuotaProductID());
                }
            }

            if (bool1 || bool2) {
                logger.info("Modification: " + record);
                modificationMapper.insertModificationRecord(new ModificationO(
                        originEntry.getQuotaEntryID(), record.toString()));
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production mode
            logger.error("Update error");
            throw e;
        }
    }

    @Transactional
    public void deleteOrder(String id) {
        try {
            quotaEntryMapper.deleteOrderProductsByEntryID(id);
            quotaEntryMapper.deleteOrderEntry(id);
        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production mode
            logger.error("Delete error");
            throw e;
        }
    }

}
