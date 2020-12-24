package org.jc.backend.service.Impl;

import org.jc.backend.dao.SalesOrderMapper;
import org.jc.backend.service.SalesOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SalesOrderServiceImpl implements SalesOrderService {
    private static final Logger logger = LoggerFactory.getLogger(SalesOrderServiceImpl.class);

    private final SalesOrderMapper salesOrderMapper;

    public SalesOrderServiceImpl(SalesOrderMapper salesOrderMapper) {
        this.salesOrderMapper = salesOrderMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */


}
