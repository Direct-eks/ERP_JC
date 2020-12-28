package org.jc.backend.service.Impl;

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

    @Transactional(readOnly = true)
    public List<ModificationO> getRecordsBySerial(String serial) {
        return modificationMapper.getRecordsBySerial(serial);
    }
}
