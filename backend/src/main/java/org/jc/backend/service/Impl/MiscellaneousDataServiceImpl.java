package org.jc.backend.service.Impl;


import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.MiscellaneousDataMapper;
import org.jc.backend.entity.MiscellaneousDataO;
import org.jc.backend.service.MiscellaneousDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MiscellaneousDataServiceImpl implements MiscellaneousDataService {
    private static final Logger logger = LoggerFactory.getLogger(MiscellaneousDataServiceImpl.class);

    private final MiscellaneousDataMapper miscellaneousDataMapper;

    public MiscellaneousDataServiceImpl(MiscellaneousDataMapper miscellaneousDataMapper) {
        this.miscellaneousDataMapper = miscellaneousDataMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional(readOnly = true)
    public List<String> getAllTaxRateOptions() {
        try {
            List<MiscellaneousDataO> taxRates = miscellaneousDataMapper.queryAllTaxRateOptions();
            return taxRates.stream().map(MiscellaneousDataO::getPropertyValue).collect(Collectors.toList());

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production
            logger.error("query failed");
            throw e;
        }
    }
}
