package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.AcceptanceMapper;
import org.jc.backend.service.AcceptanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AcceptanceServiceImpl implements AcceptanceService {

    private static final Logger logger = LoggerFactory.getLogger(AcceptanceServiceImpl.class);

    private final AcceptanceMapper acceptanceMapper;

    public AcceptanceServiceImpl(AcceptanceMapper acceptanceMapper) {
        this.acceptanceMapper = acceptanceMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    @Override
    public void createEntry() {
        try {

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("insert failed");
            throw e;
        }
    }

}
