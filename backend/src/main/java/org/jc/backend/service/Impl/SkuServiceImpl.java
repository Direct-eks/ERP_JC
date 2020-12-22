package org.jc.backend.service.Impl;

import org.jc.backend.dao.SkuMapper;
import org.jc.backend.entity.SkuWithFactoryBrandO;
import org.jc.backend.service.SkuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuServiceImpl implements SkuService {

    private static final Logger logger = LoggerFactory.getLogger(SkuServiceImpl.class);

    private final SkuMapper skuMapper;

    public SkuServiceImpl(SkuMapper skuMapper) {
        this.skuMapper = skuMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    public List<SkuWithFactoryBrandO> getFactoryBrandsByModel(int id) {
        return skuMapper.queryFactoryBrandsByModel(id);
    }
}
