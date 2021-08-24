package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jc.backend.dao.FactoryBrandMapper;
import org.jc.backend.entity.FactoryBrandO;
import org.jc.backend.service.FactoryBrandService;
import org.jc.backend.service.ModificationRecordService;
import org.jc.backend.service.UsageCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.jc.backend.service.ModificationRecordService.BRAND;
import static org.jc.backend.service.ModificationRecordService.DELETION_MSG;

@Service
public class FactoryBrandServiceImpl implements FactoryBrandService {

    private static final Logger logger = LoggerFactory.getLogger(FactoryBrandServiceImpl.class);

    private final FactoryBrandMapper factoryBrandMapper;
    private final ModificationRecordService modificationRecordService;
    private final UsageCheckService usageCheckService;

    public FactoryBrandServiceImpl(
            FactoryBrandMapper factoryBrandMapper,
            ModificationRecordService modificationRecordService,
            UsageCheckService usageCheckService
    ) {
        this.factoryBrandMapper = factoryBrandMapper;
        this.modificationRecordService = modificationRecordService;
        this.usageCheckService = usageCheckService;
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
    public void updateAllBrands(List<FactoryBrandO> updateVO) {
        Subject subject = SecurityUtils.getSubject();
        String usernameString = "修改者：" + subject.getPrincipals().getPrimaryPrincipal() + "; ";

        try {
            List<FactoryBrandO> tempBrands = new ArrayList<>(updateVO);
            List<FactoryBrandO> oldBrands = factoryBrandMapper.queryAllFactoryBrands();

            // check for added
            tempBrands.removeIf(i -> i.getFactoryBrandID() >= 0);
            for (var brand : tempBrands) {
                factoryBrandMapper.insertBrand(brand);
                logger.info("Inserted new factory brand, id: {}", brand.getFactoryBrandID());
            }

            // update all
            tempBrands = new ArrayList<>(updateVO);
            tempBrands.removeIf(i -> i.getFactoryBrandID() < 0);
            for (var brand : tempBrands) {
                StringBuilder record = new StringBuilder(usernameString);
                if (brand.formModificationRecord(brand.getOldObject(oldBrands), record)) {
                    factoryBrandMapper.updateBrand(brand);
                    logger.info("Updated brand, id: {}", brand.getFactoryBrandID());
                    modificationRecordService.insertRecord(BRAND, brand.getFactoryBrandID(), record);
                }
            }

            // check for removed
            oldBrands.removeIf(oldB -> updateVO.stream()
                    .anyMatch(b -> b.getFactoryBrandID().equals(oldB.getFactoryBrandID())));
            for (var b : oldBrands) {
                if (!usageCheckService.isBrandIDInUse(b.getFactoryBrandID())) {
                    factoryBrandMapper.deleteBrand(b.getFactoryBrandID());
                    modificationRecordService.insertRecord(BRAND, b.getFactoryBrandID(),
                            usernameString + DELETION_MSG + b);
                }
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }
}
