package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.FactoryBrandMapper;
import org.jc.backend.entity.FactoryBrandO;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.jc.backend.service.FactoryBrandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Transactional
    @Override
    public void updateAllBrands(ListUpdateVO<FactoryBrandO> updateVO) {
        try {
            List<FactoryBrandO> oldBrands = factoryBrandMapper.queryAllFactoryBrands();
            List<FactoryBrandO> tempBrands = new ArrayList<>(updateVO.getElements());

            // check for added
            tempBrands.removeIf(i -> i.getFactoryBrandID() >= 0);
            for (var brand : tempBrands) {
                factoryBrandMapper.insertBrand(brand);
            }

            // update all
            tempBrands = new ArrayList<>(updateVO.getElements());
            tempBrands.removeIf(i -> i.getFactoryBrandID() < 0);
            for (var brand : tempBrands) {
                factoryBrandMapper.updateBrand(brand);
            }

            // todo remove

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }
}
