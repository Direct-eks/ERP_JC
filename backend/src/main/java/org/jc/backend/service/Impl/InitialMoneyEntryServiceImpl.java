package org.jc.backend.service.Impl;

import org.jc.backend.dao.InitialMoneyEntryMapper;
import org.jc.backend.service.InitialMoneyEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InitialMoneyEntryServiceImpl implements InitialMoneyEntryService {
    private static final Logger logger = LoggerFactory.getLogger(InitialMoneyEntryServiceImpl.class);

    private final InitialMoneyEntryMapper initialMoneyEntryMapper;

    public InitialMoneyEntryServiceImpl(InitialMoneyEntryMapper initialMoneyEntryMapper) {
        this.initialMoneyEntryMapper = initialMoneyEntryMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */
}
