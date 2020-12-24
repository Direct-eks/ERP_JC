package org.jc.backend.service.Impl;

import org.jc.backend.dao.MoneyEntryMapper;
import org.jc.backend.service.MoneyEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MoneyEntryServiceImpl implements MoneyEntryService {
    private static final Logger logger = LoggerFactory.getLogger(MoneyEntryServiceImpl.class);

    private final MoneyEntryMapper moneyEntryMapper;

    public MoneyEntryServiceImpl(MoneyEntryMapper moneyEntryMapper) {
        this.moneyEntryMapper = moneyEntryMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */
}
