package org.jc.backend.service.Impl;

import org.jc.backend.dao.InboundEntryMapper;
import org.jc.backend.service.InboundEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InboundEntryServiceImpl implements InboundEntryService {
    private static final Logger logger = LoggerFactory.getLogger(InboundEntryServiceImpl.class);

    private final InboundEntryMapper inboundEntryMapper;

    public InboundEntryServiceImpl(InboundEntryMapper inboundEntryMapper) {
        this.inboundEntryMapper = inboundEntryMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */


}
