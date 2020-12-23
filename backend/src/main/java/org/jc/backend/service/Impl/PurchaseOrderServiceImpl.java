package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.config.exception.GlobalException;
import org.jc.backend.dao.PurchaseOrderMapper;
import org.jc.backend.entity.DO.PurchaseOrderEntryDO;
import org.jc.backend.entity.VO.PurchaseOrderEntryWithProductsVO;
import org.jc.backend.entity.PurchaseOrderProductO;
import org.jc.backend.service.PurchaseOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);

    private final PurchaseOrderMapper purchaseOrderMapper;

    public PurchaseOrderServiceImpl(PurchaseOrderMapper purchaseOrderMapper) {
        this.purchaseOrderMapper = purchaseOrderMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    public void createNewPurchaseOrder(PurchaseOrderEntryWithProductsVO entryWithProducts) throws GlobalException {
        PurchaseOrderEntryDO newEntry = new PurchaseOrderEntryDO();
        BeanUtils.copyProperties(entryWithProducts, newEntry);
        List<PurchaseOrderProductO> newProducts = entryWithProducts.getPurchaseOrderProducts();

        int count = purchaseOrderMapper.countNumberOfEntries();
        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        dateString = dateString.substring(2).replaceAll("-", "");
        String newSerial = String.format("采订%s-%03d", dateString, count + 1);
        logger.info("New serial: " + newSerial);
        newEntry.setPurchaseOrderEntryID(newSerial);
        try {
            purchaseOrderMapper.insertNewEntry(newEntry);
        } catch (PersistenceException e) {
            e.printStackTrace();
            logger.error("Insert new purchase order failed");
            throw new GlobalException("Insert new purchase order failed");
        }

        for (var product : newProducts) {
            product.setPurchaseOrderEntryID(newSerial);
            try {
                int id = purchaseOrderMapper.insertNewProduct(product);
                logger.info("Insert new purchase product id: " + id);
            } catch (PersistenceException e) {
                e.printStackTrace();
                logger.error("Insert new purchase order product failed");
                throw new GlobalException("Insert new purchase order product failed");
            }
        }
    }

    public List<PurchaseOrderEntryWithProductsVO> getOrdersWithinDateRangeByCompanyID(Date startDate, Date endDate, int id) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<PurchaseOrderEntryDO> entriesFromDatabase = purchaseOrderMapper.queryEntriesWithinDateRangeByCompanyID(
                dateFormat.format(startDate), dateFormat.format(endDate), id);

        List<PurchaseOrderEntryWithProductsVO> entries = new ArrayList<>();
        for (var entryFromDatabase : entriesFromDatabase) {
            PurchaseOrderEntryWithProductsVO tempEntry = new PurchaseOrderEntryWithProductsVO();
            BeanUtils.copyProperties(entryFromDatabase, tempEntry);

            try {
                List<PurchaseOrderProductO> products = purchaseOrderMapper.queryProductsByEntryID(
                        tempEntry.getPurchaseOrderEntryID());
                tempEntry.setPurchaseOrderProducts(products);
            } catch (PersistenceException e) {
                logger.error("Query error");
            }

            entries.add(tempEntry);
        }

        return entries;
    }
}
