package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.WarehouseInEntryMapper;
import org.jc.backend.entity.DO.WarehouseEntryDO;
import org.jc.backend.entity.VO.WarehouseEntryWithProductsVO;
import org.jc.backend.entity.WarehouseProductO;
import org.jc.backend.service.WarehouseEntryService;
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
public class WarehouseEntryServiceImpl implements WarehouseEntryService {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseEntryServiceImpl.class);

    private final WarehouseInEntryMapper warehouseInEntryMapper;

    public WarehouseEntryServiceImpl(WarehouseInEntryMapper warehouseInEntryMapper) {
        this.warehouseInEntryMapper = warehouseInEntryMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    @Override
    public void createEntry(WarehouseEntryWithProductsVO entryVO, String type, boolean isInbound) {
        try {

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("insert failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<WarehouseEntryWithProductsVO> getEntriesInDateRange(Date startDate, Date endDate,
                                                                    String type, boolean isInbound) {
        try {
            List<WarehouseEntryDO> entriesFromDatabase = warehouseInEntryMapper.queryEntriesInDateRangeByType(
                    MyUtils.dateFormat.format(startDate), MyUtils.dateFormat.format(endDate), "");

            List<WarehouseEntryWithProductsVO> entries = new ArrayList<>();
            for (var entry : entriesFromDatabase) {
                WarehouseEntryWithProductsVO tempEntry = new WarehouseEntryWithProductsVO();
                BeanUtils.copyProperties(entry, tempEntry);

                List<WarehouseProductO> products = warehouseInEntryMapper.queryProductsByEntryID(
                        tempEntry.getWarehouseEntryID());
                tempEntry.setEntryProducts(products);

                entries.add(tempEntry);
            }
            return entries;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void modifyEntry(WarehouseEntryWithProductsVO entry, String type, boolean isInbound) {
        try {

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public void getAllProductsByWarehouseStockID(int id, String type, boolean isInbound) {
        try {

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }
}
