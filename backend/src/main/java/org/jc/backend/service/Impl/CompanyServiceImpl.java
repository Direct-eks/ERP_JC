package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jc.backend.dao.CompanyMapper;
import org.jc.backend.entity.CompanyAreaO;
import org.jc.backend.entity.CompanyO;
import org.jc.backend.entity.RelevantCompanyCategoryO;
import org.jc.backend.entity.RelevantCompanyO;
import org.jc.backend.service.CompanyService;
import org.jc.backend.service.ModificationRecordService;
import org.jc.backend.service.UsageCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.jc.backend.service.ModificationRecordService.COMPANY_CATEGORY;
import static org.jc.backend.service.ModificationRecordService.COMPANY;
import static org.jc.backend.service.ModificationRecordService.R_COMPANY_CATEGORY;
import static org.jc.backend.service.ModificationRecordService.R_COMPANY;
import static org.jc.backend.service.ModificationRecordService.DELETION_MSG;

@Service
public class CompanyServiceImpl implements CompanyService {

    private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);

    private final CompanyMapper companyMapper;
    private final ModificationRecordService modificationRecordService;
    private final UsageCheckService usageCheckService;

    public CompanyServiceImpl(
            CompanyMapper companyMapper,
            ModificationRecordService modificationRecordService,
            UsageCheckService usageCheckService
    ) {
        this.companyMapper = companyMapper;
        this.modificationRecordService = modificationRecordService;
        this.usageCheckService = usageCheckService;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional(readOnly = true)
    @Override
    public List<CompanyO> getCompanyByFuzzySearch(String phone, String name) {
        try {
            return companyMapper.queryCompanyByFuzzySearch(phone, name);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<CompanyAreaO> getCompanyAreas() {
        try {
            return companyMapper.queryCompanyAreas();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<CompanyO> getCompaniesByAreaID(int id) {
        try {
            return companyMapper.queryCompaniesByAreaID(id);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public CompanyO getCompanyByID(int id) {
        try {
            return companyMapper.queryCompanyByID(id);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public CompanyO getSelfCompany() {
        try {
            return companyMapper.querySelfCompany();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateCompanyAreas(List<CompanyAreaO> updateVO) {
        Subject subject = SecurityUtils.getSubject();
        String usernameString = "修改者：" + subject.getPrincipals().getPrimaryPrincipal() + "; ";

        try {
            List<CompanyAreaO> tempAreas = new ArrayList<>(updateVO);
            List<CompanyAreaO> oldAreas = companyMapper.queryCompanyAreas();

            if (subject.isPermitted("system:partnerCompanyCategories:create")) {
                tempAreas.removeIf(a -> a.getAreaID() >= 0);
                for (var area : tempAreas) {
                    companyMapper.insertPartnerCompanyArea(area);
                    logger.info("Inserted new company area, id: {}", area.getAreaID());
                }
            }

            if (subject.isPermitted("system:partnerCompanyCategories:update")) {
                tempAreas = new ArrayList<>(updateVO);
                tempAreas.removeIf(a -> a.getAreaID() < 0);
                for (var area : tempAreas) {
                    StringBuilder record = new StringBuilder(usernameString);
                    if (area.formModificationRecord(area.getOldObject(oldAreas), record)) {
                        companyMapper.updatePartnerCompanyArea(area);
                        logger.info("Updated company area, id {}", area.getAreaID());
                        modificationRecordService.insertRecord(COMPANY_CATEGORY, area.getAreaID(), record);
                    }
                }
            }

            // check for removed
            if (subject.isPermitted("system:partnerCompanyCategories:remove")) {
                oldAreas.removeIf(oldA -> updateVO.stream()
                        .anyMatch(a -> a.getAreaID().equals(oldA.getAreaID())));
                for (var area : oldAreas) {
                    if (!usageCheckService.isPartnerCompanyCategoryIDInUse(area.getAreaID())) {
                        companyMapper.deletePartnerCompanyArea(area.getAreaID());
                        logger.info("Deleted company area, id {}", area.getAreaID());
                        modificationRecordService.insertRecord(COMPANY_CATEGORY, area.getAreaID(),
                                usernameString + DELETION_MSG + area);
                    }
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
    public void updatePartnerCompanyWithArea(int areaID, List<CompanyO> updateVO) {
        Subject subject = SecurityUtils.getSubject();
        String usernameString = "修改者：" + subject.getPrincipals().getPrimaryPrincipal() + "; ";

        try {
            List<CompanyO> tempCompanies = new ArrayList<>(updateVO);
            List<CompanyO> oldCompanies = companyMapper.queryCompaniesByAreaID(areaID);

            tempCompanies.removeIf(c -> c.getCompanyID() >= 0);
            for (var company : tempCompanies) {
                companyMapper.insertCompany(company);
                logger.info("Inserted new company, id: {}", company.getCompanyID());
            }

            tempCompanies = new ArrayList<>(updateVO);
            tempCompanies.removeIf(c -> c.getCompanyID() < 0);
            for (var company : tempCompanies) {
                StringBuilder record = new StringBuilder(usernameString);
                if (company.formModificationRecord(company.getOldObject(oldCompanies), record)) {
                    companyMapper.updateCompany(company);
                    logger.info("Updated company, id: {}", company.getCompanyID());
                    modificationRecordService.insertRecord(COMPANY, company.getCompanyID(), record);
                }
            }

            // check for remove is possible
            oldCompanies.removeIf(oldC -> updateVO.stream()
                    .anyMatch(c -> oldC.getCompanyID().equals(c.getCompanyID())));
            for (var company : oldCompanies) {
                if (!usageCheckService.isPartnerCompanyIDInUse(company.getCompanyID())) {
                    companyMapper.deleteCompany(company.getCompanyID());
                    logger.info("Deleted company, id: {}", company.getCompanyID());
                    modificationRecordService.insertRecord(COMPANY, company.getCompanyID(),
                            usernameString + DELETION_MSG + company);
                }
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<RelevantCompanyCategoryO> getRelevantCompanyCategories() {
        try {
            return companyMapper.queryRelevantCompanyCategories();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<RelevantCompanyO> getRelevantCompaniesByCategory(int id) {
        try {
            return companyMapper.queryRelevantCompaniesByCategory(id);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateRelevantCompanyCategories(List<RelevantCompanyCategoryO> updateVO) {
        Subject subject = SecurityUtils.getSubject();
        String usernameString = "修改者：" + subject.getPrincipals().getPrimaryPrincipal() + "; ";

        try {
            List<RelevantCompanyCategoryO> tempCategories = new ArrayList<>(updateVO);
            List<RelevantCompanyCategoryO> oldCategories = companyMapper.queryRelevantCompanyCategories();

            tempCategories.removeIf(c -> c.getCategoryID() >= 0);
            for (var category : tempCategories) {
                companyMapper.insertRelevantCompanyCategory(category);
                logger.info("Inserted new relevant company category, id: {}", category.getCategoryID());
            }

            tempCategories = new ArrayList<>(updateVO);
            tempCategories.removeIf(c -> c.getCategoryID() < 0);
            for (var category : tempCategories) {
                StringBuilder record = new StringBuilder(usernameString);
                if (category.formModificationRecord(category.getOldObject(oldCategories), record)) {
                    companyMapper.updateRelevantCompanyCategory(category);
                    logger.info("Updated relevant company category, id: {}", category.getCategoryID());
                    modificationRecordService.insertRecord(R_COMPANY_CATEGORY, category.getCategoryID(), record);
                }
            }

            // check for removed
            oldCategories.removeIf(oldC -> updateVO.stream()
                    .anyMatch(c -> c.getCategoryID().equals(oldC.getCategoryID())));
            for (var category : oldCategories) {
                if (!usageCheckService.isRelevantCompanyCategoryIDInUse(category.getCategoryID())) {
                    companyMapper.deleteRelevantCompanyCategory(category.getCategoryID());
                    logger.info("Deleted relevant company category, id: {}", category.getCategoryID());
                    modificationRecordService.insertRecord(R_COMPANY_CATEGORY, category.getCategoryID(),
                            usernameString + DELETION_MSG + category);
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
    public void updateRelevantCompanyWithCategory(int categoryID, List<RelevantCompanyO> updateVO) {
        Subject subject = SecurityUtils.getSubject();
        String usernameString = "修改者：" + subject.getPrincipals().getPrimaryPrincipal() + "; ";

        try {
            List<RelevantCompanyO> tempCompanies = new ArrayList<>(updateVO);
            List<RelevantCompanyO> oldCompanies = companyMapper.queryRelevantCompaniesByCategory(categoryID);

            tempCompanies.removeIf(c -> c.getCompanyID() >= 0);
            for (var company : tempCompanies) {
                companyMapper.insertRelevantCompany(company);
                logger.info("Inserted new relevant company, id: {}", company.getCompanyID());
            }

            tempCompanies = new ArrayList<>(updateVO);
            tempCompanies.removeIf(c -> c.getCompanyID() < 0);
            for (var company : tempCompanies) {
                StringBuilder record = new StringBuilder(usernameString);
                if (company.formModificationRecord(company.getOldObject(oldCompanies), record)) {
                    companyMapper.updateRelevantCompany(company);
                    logger.info("Updated relevant company, id: {}", company.getCompanyID());
                    modificationRecordService.insertRecord(R_COMPANY, company.getCompanyID(), record);
                }
            }

            // check for remove is possible
            oldCompanies.removeIf(oldC -> updateVO.stream()
                    .anyMatch(c -> oldC.getCompanyID().equals(c.getCompanyID())));
            for (var company : oldCompanies) {
                if (!usageCheckService.isRelevantCompanyIDInUse(company.getCompanyID())) {
                    companyMapper.deleteRelevantCompany(company.getCompanyID());
                    logger.info("Deleted relevant company, id: {}", company.getCompanyID());
                    modificationRecordService.insertRecord(R_COMPANY, company.getCompanyID(),
                            usernameString + DELETION_MSG + company);
                }
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }
}
