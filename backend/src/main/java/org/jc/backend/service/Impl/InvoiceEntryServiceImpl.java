package org.jc.backend.service.Impl;

import org.jc.backend.dao.InvoiceEntryMapper;
import org.jc.backend.service.InvoiceEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InvoiceEntryServiceImpl implements InvoiceEntryService {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceEntryServiceImpl.class);

    private final InvoiceEntryMapper invoiceEntryMapper;

    public InvoiceEntryServiceImpl(InvoiceEntryMapper invoiceEntryMapper) {
        this.invoiceEntryMapper = invoiceEntryMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */
}
