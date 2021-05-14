package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.FactoryBrandMapper;
import org.jc.backend.entity.FactoryBrandO;
import org.jc.backend.service.FactoryBrandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FactoryBrandServiceImpl implements FactoryBrandService {

    private static final Logger logger = LoggerFactory.getLogger(FactoryBrandServiceImpl.class);

    private final FactoryBrandMapper factoryBrandMapper;

    public FactoryBrandServiceImpl(FactoryBrandMapper factoryBrandMapper) {
        this.factoryBrandMapper = factoryBrandMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional(readOnly = true)
    @Override
    public List<FactoryBrandO> getAllFactoryBrands() {
        try {
            return factoryBrandMapper.queryAllFactoryBrands();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }
}
