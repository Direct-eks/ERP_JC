package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.MeasurementUnitMapper;
import org.jc.backend.entity.MeasurementUnitO;
import org.jc.backend.service.MeasurementUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MeasurementUnitServiceImpl implements MeasurementUnitService {

    private static final Logger logger = LoggerFactory.getLogger(MeasurementUnitServiceImpl.class);

    private final MeasurementUnitMapper measurementUnitMapper;

    public MeasurementUnitServiceImpl(MeasurementUnitMapper measurementUnitMapper) {
        this.measurementUnitMapper = measurementUnitMapper;
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
}
