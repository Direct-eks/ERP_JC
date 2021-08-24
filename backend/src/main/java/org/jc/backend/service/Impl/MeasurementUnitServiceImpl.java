package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jc.backend.dao.MeasurementUnitMapper;
import org.jc.backend.entity.MeasurementUnitO;
import org.jc.backend.service.MeasurementUnitService;
import org.jc.backend.service.ModificationRecordService;
import org.jc.backend.service.UsageCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.jc.backend.service.ModificationRecordService.DELETION_MSG;
import static org.jc.backend.service.ModificationRecordService.UNIT;

@Service
public class MeasurementUnitServiceImpl implements MeasurementUnitService {

    private static final Logger logger = LoggerFactory.getLogger(MeasurementUnitServiceImpl.class);

    private final MeasurementUnitMapper measurementUnitMapper;
    private final ModificationRecordService modificationRecordService;
    private final UsageCheckService usageCheckService;

    public MeasurementUnitServiceImpl(
            MeasurementUnitMapper measurementUnitMapper,
            ModificationRecordService modificationRecordService,
            UsageCheckService usageCheckService) {
        this.measurementUnitMapper = measurementUnitMapper;
        this.modificationRecordService = modificationRecordService;
        this.usageCheckService = usageCheckService;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional(readOnly = true)
    @Override
    public List<MeasurementUnitO> getAllUnits() {
        try {
            return measurementUnitMapper.queryAllUnits();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query error");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateUnits(List<MeasurementUnitO> updateVO) {
        Subject subject = SecurityUtils.getSubject();
        String usernameString = "修改者：" + subject.getPrincipals().getPrimaryPrincipal() + "; ";

        try {
            List<MeasurementUnitO> tempUnits = new ArrayList<>(updateVO);
            List<MeasurementUnitO> oldUnits = measurementUnitMapper.queryAllUnits();

            // check for added
            tempUnits.removeIf(i -> i.getUnitID() >= 0);
            for (var unit : tempUnits) {
                measurementUnitMapper.insertUnit(unit);
                logger.info("Inserted new unit, id: {}", unit.getUnitID());
            }

            // update all
            tempUnits = new ArrayList<>(updateVO);
            tempUnits.removeIf(i -> i.getUnitID() < 0);
            for (var unit : tempUnits) {
                StringBuilder record = new StringBuilder(usernameString);
                if (unit.formModificationRecord(unit.getOldObject(oldUnits), record)) {
                    measurementUnitMapper.updateUnit(unit);
                    logger.info("Updated unit, id: {}", unit.getUnitID());
                    modificationRecordService.insertRecord(UNIT, unit.getUnitID(), record);
                }
            }

            // check for removed
            oldUnits.removeIf(oldU -> updateVO.stream()
                    .anyMatch(u -> u.getUnitID().equals(oldU.getUnitID())));
            for (var u : oldUnits) {
                if (!usageCheckService.isUnitIDInUse(u.getUnitID())) {
                    measurementUnitMapper.deleteUnit(u.getUnitID());
                    logger.info("Deleted unit, id: {}", u.getUnitID());
                    modificationRecordService.insertRecord(UNIT, u.getUnitID(),
                            usernameString + DELETION_MSG + u);
                }
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update error");
            throw e;
        }
    }
}
