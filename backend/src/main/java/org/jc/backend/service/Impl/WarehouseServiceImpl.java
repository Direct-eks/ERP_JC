package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jc.backend.dao.WarehouseMapper;
import org.jc.backend.entity.WarehouseO;
import org.jc.backend.service.ModificationRecordService;
import org.jc.backend.service.UsageCheckService;
import org.jc.backend.service.WarehouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.jc.backend.service.ModificationRecordService.DELETION_MSG;
import static org.jc.backend.service.ModificationRecordService.WAREHOUSE;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseServiceImpl.class);

    private final WarehouseMapper warehouseMapper;
    private final ModificationRecordService modificationRecordService;
    private final UsageCheckService usageCheckService;

    public WarehouseServiceImpl(
            WarehouseMapper warehouseMapper,
            ModificationRecordService modificationRecordService,
            UsageCheckService usageCheckService
    ) {
        this.warehouseMapper = warehouseMapper;
        this.modificationRecordService = modificationRecordService;
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
        Subject subject = SecurityUtils.getSubject();
        String usernameString = "修改者：" + subject.getPrincipals().getPrimaryPrincipal() + "; ";

        try {
            List<WarehouseO> tempWarehouses = new ArrayList<>(updateVO);
            List<WarehouseO> oldWarehouses = warehouseMapper.queryWarehouses();

            // check for added
            tempWarehouses.removeIf(w -> w.getWarehouseID() >= 0);
            for (var warehouse : tempWarehouses) {
                warehouseMapper.insertWarehouse(warehouse);
                logger.info("Inserted new warehouse, id: {}", warehouse.getWarehouseID());
            }

            tempWarehouses = new ArrayList<>(updateVO);
            tempWarehouses.removeIf(w -> w.getWarehouseID() < 0);
            for (var warehouse : tempWarehouses) {
                StringBuilder record = new StringBuilder(usernameString);
                if (warehouse.formModificationRecord(warehouse.getOldObject(oldWarehouses), record)) {
                    warehouseMapper.updateWarehouse(warehouse);
                    logger.info("Updated warehouse, id: {}", warehouse.getWarehouseID());
                    modificationRecordService.insertRecord(WAREHOUSE, warehouse.getWarehouseID(), record);
                }
            }

            // check for removed
            oldWarehouses.removeIf(oldW -> updateVO.stream()
                    .anyMatch(w -> w.getWarehouseID().equals(oldW.getWarehouseID())));
            for (var w : oldWarehouses) {
                if (!usageCheckService.isWarehouseIDInUse(w.getWarehouseID())) {
                    warehouseMapper.deleteWarehouse(w.getWarehouseID());
                    logger.info("Deleted warehouse, id: {}", w.getWarehouseID());
                    modificationRecordService.insertRecord(WAREHOUSE, w.getWarehouseID(),
                            usernameString + DELETION_MSG + w);
                }
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }
}
