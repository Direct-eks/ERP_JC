package org.jc.backend.service.Impl;

import org.jc.backend.dao.ShippingCostEntryMapper;
import org.jc.backend.service.ShippingCostEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ShippingCostEntryServiceImpl implements ShippingCostEntryService {
    private static final Logger logger = LoggerFactory.getLogger(ShippingCostEntryServiceImpl.class);

    private final ShippingCostEntryMapper shippingCostEntryMapper;

    public ShippingCostEntryServiceImpl(ShippingCostEntryMapper shippingCostEntryMapper) {
        this.shippingCostEntryMapper = shippingCostEntryMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */
}
