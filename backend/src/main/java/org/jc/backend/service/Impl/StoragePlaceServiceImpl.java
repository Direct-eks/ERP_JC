package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.StoragePlaceMapper;
import org.jc.backend.entity.StoragePlaceO;
import org.jc.backend.service.StoragePlaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StoragePlaceServiceImpl implements StoragePlaceService {

    private static final Logger logger = LoggerFactory.getLogger(StoragePlaceServiceImpl.class);

    private final StoragePlaceMapper storagePlaceMapper;

    public StoragePlaceServiceImpl(StoragePlaceMapper storagePlaceMapper) {
        this.storagePlaceMapper = storagePlaceMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional(readOnly = true)
    @Override
    public List<StoragePlaceO> getAllPlaces() {
        try {
            return storagePlaceMapper.queryAllPlaces();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void addNewPlace(StoragePlaceO storagePlaceO) {
        try {

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void changePlace(StoragePlaceO storagePlaceO) {
        try {

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }
}
