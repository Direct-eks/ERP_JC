package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jc.backend.dao.FeesMapper;
import org.jc.backend.entity.DO.FeeEntryDO;
import org.jc.backend.entity.FeeCategoryO;
import org.jc.backend.entity.FeeEntryDetailO;
import org.jc.backend.entity.VO.FeeEntryWithDetailVO;
import org.jc.backend.service.FeesService;
import org.jc.backend.service.ModificationRecordService;
import org.jc.backend.service.UsageCheckService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.jc.backend.service.ModificationRecordService.DELETION_MSG;
import static org.jc.backend.service.ModificationRecordService.FEE_CATEGORY;

@Service
public class FeesServiceImpl implements FeesService {
    private static final Logger logger = LoggerFactory.getLogger(FeesServiceImpl.class);

    private final FeesMapper feesMapper;
    private final ModificationRecordService modificationRecordService;
    private final UsageCheckService usageCheckService;

    public FeesServiceImpl(
            FeesMapper feesMapper,
            ModificationRecordService modificationRecordService,
            UsageCheckService usageCheckService
    ) {
        this.feesMapper = feesMapper;
        this.modificationRecordService = modificationRecordService;
        this.usageCheckService = usageCheckService;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional(readOnly = true)
    @Override
    public List<FeeCategoryO> getFeeCategories() {
        try {
            return feesMapper.queryAllCategories();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateFeeCategories(List<FeeCategoryO> updateVO) {
        Subject subject = SecurityUtils.getSubject();
        String usernameString = "修改者：" + subject.getPrincipals().getPrimaryPrincipal() + "; ";

        try {
            List<FeeCategoryO> tempCategories = new ArrayList<>(updateVO);
            List<FeeCategoryO> oldCategories = feesMapper.queryAllCategories();

            tempCategories.removeIf(c -> c.getFeeCategoryID() >= 0);
            for (var c : tempCategories) {
                feesMapper.insertCategory(c);
                logger.info("Inserted new fee category, id: {}", c.getFeeCategoryID());
            }

            tempCategories = new ArrayList<>(updateVO);
            tempCategories.removeIf(c -> c.getFeeCategoryID() < 0);
            for (var c : tempCategories) {
                StringBuilder record = new StringBuilder(usernameString);
                if (c.formModificationRecord(c.getOldObject(oldCategories), record)) {
                    feesMapper.updateCategory(c);
                    logger.info("Updated fee category, id: {}", c.getFeeCategoryID());
                    modificationRecordService.insertRecord(FEE_CATEGORY, c.getFeeCategoryID(), record);
                }
            }

            oldCategories.removeIf(oldC -> updateVO.stream()
                    .anyMatch(c -> c.getFeeCategoryID().equals(oldC.getFeeCategoryID())));
            for (var c : oldCategories) {
                if (!usageCheckService.isFeeCategoryIDInUse(c.getFeeCategoryID())) {
                    feesMapper.deleteCategory(c.getFeeCategoryID());
                    logger.info("Deleted fee category, id: {}", c);
                    modificationRecordService.insertRecord(FEE_CATEGORY, c.getFeeCategoryID(),
                            usernameString + DELETION_MSG + c);
                }
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }


    @Transactional
    @Override
    public void createEntry(FeeEntryWithDetailVO entryWithDetailVO, String prefix) {
        try {
            FeeEntryDO feeEntryDO = new FeeEntryDO();
            BeanUtils.copyProperties(entryWithDetailVO, feeEntryDO);
            List<FeeEntryDetailO> detailList = new ArrayList<>(entryWithDetailVO.getFeeDetails());

            String entryDate = feeEntryDO.getEntryDate();
            int count = feesMapper.countNumberOfEntriesOfGivenDate(entryDate, prefix);
            String newSerial = MyUtils.formNewSerial(prefix, count, entryDate);

            feeEntryDO.setFeeEntryID(newSerial);
            feesMapper.insertNewEntry(feeEntryDO);

            for (var detailO : detailList) {
                detailO.setFeeEntryID(newSerial);
                feesMapper.insertNewDetail(detailO);
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("insert failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateEntry(FeeEntryWithDetailVO entryWithDetailVO) {
        try {

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }
}
