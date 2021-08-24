package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.ModificationMapper;
import org.jc.backend.entity.ModificationO;
import org.jc.backend.service.ModificationRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ModificationRecordServiceImpl implements ModificationRecordService {

    private static final Logger logger = LoggerFactory.getLogger(ModificationRecordServiceImpl.class);

    private final ModificationMapper modificationMapper;



    public ModificationRecordServiceImpl(ModificationMapper modificationMapper) {
        this.modificationMapper = modificationMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    @Override
    public void insertRecord(String serial, String content) {
        try {
            ModificationO record = new ModificationO(serial, content);
            modificationMapper.insertEntryModificationRecord(record);
            logger.info("New Entry Modification Record Created: {}", record);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Insert failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void insertRecord(String serial, StringBuilder content) {
        insertRecord(serial, content.toString());
    }

    @Transactional
    @Override
    public void insertRecord(String category, int id, String content) {
        try {
            ModificationO record = new ModificationO(category, id, content);
            modificationMapper.insertMiscModificationRecord(record);
            logger.info("New Misc Modification Record Created: {}", record);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Insert failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void insertRecord(String category, int id, StringBuilder content) {
        insertRecord(category, id, content.toString());
    }

    @Transactional(readOnly = true)
    @Override
    public List<ModificationO> getEntryRecordsBySerial(String serial) {
        try {
            return modificationMapper.getEntryRecordsBySerial(serial);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ModificationO> getMiscRecordsByCategoryAndID(String category, int id) {
        try {
            return modificationMapper.getMiscRecordsBySerial(category, id);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }
}
