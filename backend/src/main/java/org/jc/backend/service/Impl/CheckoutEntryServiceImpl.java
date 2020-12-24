package org.jc.backend.service.Impl;

import org.jc.backend.dao.CheckoutEntryMapper;
import org.jc.backend.service.CheckoutEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CheckoutEntryServiceImpl implements CheckoutEntryService {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutEntryServiceImpl.class);

    private final CheckoutEntryMapper checkoutEntryMapper;

    public CheckoutEntryServiceImpl(CheckoutEntryMapper checkoutEntryMapper) {
        this.checkoutEntryMapper = checkoutEntryMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */
}
