package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jc.backend.dao.FeesMapper;
import org.jc.backend.entity.DO.FeeEntryDO;
import org.jc.backend.entity.FeeCategoryO;
import org.jc.backend.entity.FeeEntryDetailO;
import org.jc.backend.entity.VO.FeeEntryWithDetailVO;
import org.jc.backend.service.BankAccountStatService;
import org.jc.backend.service.FeesService;
import org.jc.backend.service.ModificationRecordService;
import org.jc.backend.service.UsageCheckService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.jc.backend.service.ModificationRecordService.DELETION_MSG;
import static org.jc.backend.service.ModificationRecordService.FEE_CATEGORY;

@Service
public class FeesServiceImpl implements FeesService {
    private static final Logger logger = LoggerFactory.getLogger(FeesServiceImpl.class);

    private final FeesMapper feesMapper;
    private final ModificationRecordService modificationRecordService;
    private final UsageCheckService usageCheckService;
    private final BankAccountStatService acceptanceService;
    private final BankAccountStatService moneyEntryService;

    public FeesServiceImpl(
            FeesMapper feesMapper,
            ModificationRecordService modificationRecordService,
            UsageCheckService usageCheckService,
            @Qualifier("acceptanceServiceImpl") BankAccountStatService acceptanceServiceImpl,
            @Qualifier("moneyEntryServiceImpl") BankAccountStatService moneyEntryServiceImpl
    ) {
        this.feesMapper = feesMapper;
        this.modificationRecordService = modificationRecordService;
        this.usageCheckService = usageCheckService;
        this.acceptanceService = acceptanceServiceImpl;
        this.moneyEntryService = moneyEntryServiceImpl;
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
    public void createEntry(FeeEntryWithDetailVO entryWithDetailVO, String prefix, boolean hasDetails) {
        try {
            FeeEntryDO feeEntryDO = new FeeEntryDO();
            BeanUtils.copyProperties(entryWithDetailVO, feeEntryDO);
            List<FeeEntryDetailO> detailList = new ArrayList<>(entryWithDetailVO.getFeeDetails());

            String entryDate = feeEntryDO.getEntryDate();
            int count = feesMapper.countNumberOfEntriesOfGivenDate(entryDate, prefix);
            String newSerial = MyUtils.formNewSerial(prefix, count, entryDate);

            feeEntryDO.setFeeEntryID(newSerial);
            feesMapper.insertNewEntry(feeEntryDO);
            logger.info("Inserted new fee entry: {}", newSerial);

            if (hasDetails) {
                for (var detailO : detailList) {
                    detailO.setFeeEntryID(newSerial);
                    feesMapper.insertNewDetail(detailO);
                    logger.info("Inserted new fee details: {}", detailO.getFeeDetailEntryID());
                }
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("insert failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<FeeEntryWithDetailVO> getEntriesInDateRange(String startDate, String endDate,
                                                            String prefix, boolean hasDetails) {
        try {
            List<FeeEntryDO> entries = feesMapper.queryEntriesInDateRange(startDate, endDate, prefix);
            List<FeeEntryWithDetailVO> entryVOs = new ArrayList<>(entries.size());

            if (hasDetails) {
                for (var entry : entries) {
                    FeeEntryWithDetailVO vo = new FeeEntryWithDetailVO();
                    BeanUtils.copyProperties(entry, vo);
                    var details = feesMapper.queryDetailsByEntryID(entry.getFeeEntryID());
                    vo.setFeeDetails(details);
                    entryVOs.add(vo);
                }
            }
            else {
                for (var entry : entries) {
                    FeeEntryWithDetailVO vo = new FeeEntryWithDetailVO();
                    BeanUtils.copyProperties(entry, vo);
                    entryVOs.add(vo);
                }
            }
            return entryVOs;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateEntry(FeeEntryWithDetailVO entryWithDetailVO, boolean containsDetail) {
        Subject subject = SecurityUtils.getSubject();
        String usernameString = "修改者：" + subject.getPrincipals().getPrimaryPrincipal() + "; ";

        try {
            FeeEntryDO modifiedDO = new FeeEntryDO();
            BeanUtils.copyProperties(entryWithDetailVO, modifiedDO);

            String feeEntryID = entryWithDetailVO.getFeeEntryID();
            FeeEntryDO originalDO = feesMapper.selectEntryForCompare(feeEntryID);

            StringBuilder record = new StringBuilder(usernameString);
            boolean entryChanged = false;
            if (this.formModificationRecord(record, modifiedDO, originalDO)) {
                entryChanged = true;
                feesMapper.updateEntry(modifiedDO);
                logger.info("Updated fees entry: {}", feeEntryID);
            }

            if (containsDetail) {
                List<FeeEntryDetailO> modifiedDetails = entryWithDetailVO.getFeeDetails();
                List<FeeEntryDetailO> originalDetails = feesMapper.queryDetailsByEntryID(feeEntryID);

                boolean productsChanged = false;
                for (var originalDetail : originalDetails) {
                    boolean found = false;
                    for (var modifiedDetail : modifiedDetails) {
                        if (Objects.equals(modifiedDetail.getFeeDetailEntryID(), originalDetail.getFeeDetailEntryID())) {
                            if (this.formModificationRecord(record, modifiedDetail, originalDetail)) {
                                productsChanged = true;
                                feesMapper.updateEntryDetail(modifiedDetail);
                                logger.info("Updated fee detail: {}", originalDetail.getFeeDetailEntryID());
                            }
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        logger.error("deleted detail found, currently not supported");
                        throw new RuntimeException();
                    }
                }

                if (entryChanged || productsChanged) {
                    modificationRecordService.insertRecord(feeEntryID, record);
                }
                else {
                    logger.warn("nothing changed, begin rolling back");
                    throw new RuntimeException();
                }
            }
            else {
                if (entryChanged) {
                    modificationRecordService.insertRecord(feeEntryID, record);
                }
                else {
                    logger.warn("nothing changed, begin rolling back");
                    throw new RuntimeException();
                }
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    private boolean formModificationRecord(
            StringBuilder record, FeeEntryDO modifiedDO, FeeEntryDO originalDO) {
        boolean bool = false;

        if (new BigDecimal(modifiedDO.getAmount())
                .compareTo(new BigDecimal(originalDO.getAmount())) != 0) {
            bool = true;
            record.append(String.format("金额: %s -> %s; ",
                    originalDO.getAmount(), modifiedDO.getAmount()));
        }
        if (!modifiedDO.getNumber().equals(originalDO.getNumber())) {
            bool = true;
            record.append(String.format("号码: %s -> %s; ",
                    originalDO.getNumber(), originalDO.getNumber()));
        }
        if (modifiedDO.getDepartmentID() != originalDO.getDepartmentID()) {
            bool = true;
            record.append(String.format("部门: %s -> %s; ",
                    originalDO.getDepartmentName(), modifiedDO.getDepartmentName()));
        }
        if (!modifiedDO.getRemark().equals(originalDO.getRemark())) {
            bool = true;
            record.append(String.format("摘要: %s -> %s; ",
                    originalDO.getRemark(), modifiedDO.getRemark()));
        }
        if (modifiedDO.getIsBookKeeping() != originalDO.getIsBookKeeping()) {
            bool = true;
            record.append(String.format("记账: %d -> %d; ",
                    originalDO.getIsBookKeeping(), modifiedDO.getIsBookKeeping()));
        }
        if (modifiedDO.getIsVerified() != originalDO.getIsVerified()) {
            bool = true;
            record.append(String.format("审核: %d -> %d; ",
                    originalDO.getIsVerified(), modifiedDO.getIsVerified()));
        }

        return bool;
    }

    private boolean formModificationRecord(
            StringBuilder record, FeeEntryDetailO modifiedO, FeeEntryDetailO originalO) {
        boolean bool = false;

        int id = originalO.getFeeDetailEntryID();
        if (!modifiedO.getRemark().equals(originalO.getRemark())) {
            bool = true;
            record.append(String.format("条目(%d) 摘要: %s -> %s; ", id,
                    originalO.getRemark(), modifiedO.getRemark()));
        }
        if (new BigDecimal(modifiedO.getAmount())
                .compareTo(new BigDecimal(originalO.getAmount())) != 0) {
            bool = true;
            record.append(String.format("条目(%d) 金额: %s -> %s; ", id,
                    originalO.getAmount(), modifiedO.getAmount()));
        }

        return bool;
    }

    public void calculateBalance(int bankID) {
        var acceptanceInEntries = acceptanceService.getFeeDetails(bankID, true);
        var acceptanceOutEntries = acceptanceService.getFeeDetails(bankID, false);

        var moneyInEntries = moneyEntryService.getFeeDetails(bankID, true);
        var moneyOutEntries = moneyEntryService.getFeeDetails(bankID, false);
    }
}
