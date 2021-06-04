package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.WarehouseMapper;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.jc.backend.entity.WarehouseO;
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

    public WarehouseServiceImpl(WarehouseMapper warehouseMapper) {
        this.warehouseMapper = warehouseMapper;
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
    public void updateWarehouses(ListUpdateVO<WarehouseO> updateVO) {
        try {
            List<WarehouseO> tempWarehouses = new ArrayList<>(updateVO.getElements());

            // check for added
            tempWarehouses.removeIf(w -> w.getWarehouseID() >= 0);
            for (var warehouse : tempWarehouses) {
                warehouseMapper.insertWarehouse(warehouse);
            }

            tempWarehouses = new ArrayList<>(updateVO.getElements());
            tempWarehouses.removeIf(w -> w.getWarehouseID() < 0);
            for (var warehouse : tempWarehouses) {
                warehouseMapper.updateWarehouse(warehouse);
            }

            // todo remove
            List<WarehouseO> oldWarehouses = warehouseMapper.queryWarehouses();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }
}
