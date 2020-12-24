package org.jc.backend.service.Impl;

import org.jc.backend.dao.QuotaEntryMapper;
import org.jc.backend.service.QuotaEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class QuotaEntryServiceImpl implements QuotaEntryService {
    private static final Logger logger = LoggerFactory.getLogger(QuotaEntryServiceImpl.class);

    private final QuotaEntryMapper quotaEntryMapper;

    public QuotaEntryServiceImpl(QuotaEntryMapper quotaEntryMapper) {
        this.quotaEntryMapper = quotaEntryMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */


}
