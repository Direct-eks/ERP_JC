package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.WarehouseEntryMapper;
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

@Service("productionEntryService")
public class ProductionEntryServiceImpl implements WarehouseEntryService {

    private static final Logger logger = LoggerFactory.getLogger(ProductionEntryServiceImpl.class);

    private final WarehouseEntryMapper warehouseEntryMapper;

    public ProductionEntryServiceImpl(WarehouseEntryMapper warehouseEntryMapper) {
        this.warehouseEntryMapper = warehouseEntryMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    @Override
    public void createEntry(WarehouseEntryWithProductsVO entryVO) {
        try {

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("insert failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<WarehouseEntryWithProductsVO> getEntriesInDateRange(Date startDate, Date endDate) {
        try {
            List<WarehouseEntryDO> entriesFromDatabase = warehouseEntryMapper.queryEntriesInDateRangeByType(
                    MyUtils.dateFormat.format(startDate), MyUtils.dateFormat.format(endDate), "");

            List<WarehouseEntryWithProductsVO> entries = new ArrayList<>();
            for (var entry : entriesFromDatabase) {
                WarehouseEntryWithProductsVO tempEntry = new WarehouseEntryWithProductsVO();
                BeanUtils.copyProperties(entry, tempEntry);

                List<WarehouseProductO> products = warehouseEntryMapper.queryProductsByEntryID(
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
    public void modifyEntry(WarehouseEntryWithProductsVO entry) {
        try {

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

}
