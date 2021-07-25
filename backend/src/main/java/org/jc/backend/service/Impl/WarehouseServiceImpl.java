package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.WarehouseMapper;
import org.jc.backend.entity.WarehouseO;
import org.jc.backend.service.UsageCheckService;
import org.jc.backend.service.WarehouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseServiceImpl.class);

    private final WarehouseMapper warehouseMapper;
    private final UsageCheckService usageCheckService;

    public WarehouseServiceImpl(
            WarehouseMapper warehouseMapper,
            UsageCheckService usageCheckService
    ) {
        this.warehouseMapper = warehouseMapper;
        this.usageCheckService = usageCheckService;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional(readOnly = true)
    @Override
    public List<WarehouseO> getWarehouseOptions() {
        try {
            return warehouseMapper.queryWarehouses();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateWarehouses(List<WarehouseO> updateVO) {
        try {
            List<WarehouseO> tempWarehouses = new ArrayList<>(updateVO);

            // check for added
            tempWarehouses.removeIf(w -> w.getWarehouseID() >= 0);
            for (var warehouse : tempWarehouses) {
                warehouseMapper.insertWarehouse(warehouse);
            }

            tempWarehouses = new ArrayList<>(updateVO);
            tempWarehouses.removeIf(w -> w.getWarehouseID() < 0);
            for (var warehouse : tempWarehouses) {
                warehouseMapper.updateWarehouse(warehouse);
            }

            // check for removed
            List<WarehouseO> oldWarehouses = warehouseMapper.queryWarehouses();
            oldWarehouses.removeIf(oldW -> updateVO.stream()
                    .anyMatch(w -> w.getWarehouseID() == oldW.getWarehouseID()));
            for (var w : oldWarehouses) {
                if (!usageCheckService.isWarehouseIDInUse(w.getWarehouseID())) {
                    warehouseMapper.deleteWarehouse(w.getWarehouseID());
                }
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }
}
