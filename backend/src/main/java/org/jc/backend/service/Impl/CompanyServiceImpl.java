package org.jc.backend.service.Impl;

import org.jc.backend.dao.CompanyMapper;
import org.jc.backend.entity.CompanyCategoryO;
import org.jc.backend.entity.CompanyO;
import org.jc.backend.entity.RelevantCompanyCategoryO;
import org.jc.backend.entity.RelevantCompanyO;
import org.jc.backend.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<CompanyO> getCompanyByFuzzySearch(String phone, String name) {
        return companyMapper.queryCompanyByFuzzySearch(phone, name);
    }

    @Transactional(readOnly = true)
    public List<CompanyCategoryO> getCompanyCategories() {
        return companyMapper.queryCompanyCategories();
    }
    
    @Transactional(readOnly = true)
    public List<CompanyO>getCompaniesByCategory(int id) {
         return companyMapper.queryCompaniesByCategory(id);
    }

    @Transactional(readOnly = true)
    public CompanyO getSelfCompany() {
        return companyMapper.querySelfCompany();
    }

    @Transactional(readOnly = true)
    public List<RelevantCompanyCategoryO> getRelevantCompanyCategories() {
        return companyMapper.queryRelevantCompanyCategories();
    }

    @Transactional(readOnly = true)
    public List<RelevantCompanyO> getRelevantCompaniesByCategory(int id) {
        return companyMapper.queryRelevantCompaniesByCategory(id);
    }
}
