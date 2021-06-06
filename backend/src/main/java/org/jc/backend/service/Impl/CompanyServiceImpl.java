package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.CompanyMapper;
import org.jc.backend.entity.CompanyCategoryO;
import org.jc.backend.entity.CompanyO;
import org.jc.backend.entity.RelevantCompanyCategoryO;
import org.jc.backend.entity.RelevantCompanyO;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.jc.backend.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);

    private final CompanyMapper companyMapper;

    public CompanyServiceImpl(CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
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
    public List<CompanyCategoryO> getCompanyCategories() {
        try {
            return companyMapper.queryCompanyCategories();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<CompanyO>getCompaniesByCategory(int id) {
        try {
            return companyMapper.queryCompaniesByCategory(id);

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
    public void updatePartnerCompanyWithArea(int areaID, ListUpdateVO<CompanyO> updateVO) {
        try {
            List<CompanyO> tempCompanies = new ArrayList<>(updateVO.getElements());

            tempCompanies.removeIf(c -> c.getCompanyID() >= 0);
            for (var company : tempCompanies) {
                companyMapper.insertCompany(company);
            }

            tempCompanies = new ArrayList<>(updateVO.getElements());
            tempCompanies.removeIf(c -> c.getCompanyID() < 0);
            for (var company : tempCompanies) {
                companyMapper.updateCompany(company);
            }

            // check for remove is possible
            List<CompanyO> oldCompanies = companyMapper.queryCompaniesByCategory(areaID);
            oldCompanies.removeIf(oldC -> updateVO.getElements().stream()
                    .anyMatch(c -> oldC.getCompanyID().equals(c.getCompanyID())));
            for (var company : oldCompanies) {
                // todo remove
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
    public void updateRelevantCompanyWithCategory(int categoryID, ListUpdateVO<RelevantCompanyO> updateVO) {
        try {
            List<RelevantCompanyO> tempCompanies = new ArrayList<>(updateVO.getElements());

            tempCompanies.removeIf(c -> c.getCompanyID() >= 0);
            for (var company : tempCompanies) {
                companyMapper.insertRelevantCompany(company);
            }

            tempCompanies = new ArrayList<>(updateVO.getElements());
            tempCompanies.removeIf(c -> c.getCompanyID() < 0);
            for (var company : tempCompanies) {
                companyMapper.updateRelevantCompany(company);
            }

            // check for remove is possible
            List<RelevantCompanyO> oldCompanies = companyMapper.queryRelevantCompaniesByCategory(categoryID);
            oldCompanies.removeIf(oldC -> updateVO.getElements().stream()
                    .anyMatch(c -> oldC.getCompanyID().equals(c.getCompanyID())));
            for (var company : oldCompanies) {
                // todo remove
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }
}
