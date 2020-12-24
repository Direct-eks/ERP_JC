package org.jc.backend.service.Impl;

import org.jc.backend.dao.OutboundEntryMapper;
import org.jc.backend.service.OutboundEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OutboundEntryServiceImpl implements OutboundEntryService {
    private static final Logger logger = LoggerFactory.getLogger(OutboundEntryServiceImpl.class);

    private final OutboundEntryMapper outboundEntryMapper;

    public OutboundEntryServiceImpl(OutboundEntryMapper outboundEntryMapper) {
        this.outboundEntryMapper = outboundEntryMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */


}
