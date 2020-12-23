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
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);

    private final CompanyMapper companyMapper;

    public CompanyServiceImpl(CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    public List<CompanyO> getCompanyByFuzzySearch(CompanyO companyO) {
        String phoneQuery = StringUtils.trimAllWhitespace(companyO.getPhone());
        phoneQuery = StringUtils.hasLength(phoneQuery) ? phoneQuery + "%" : phoneQuery;
        String nameQuery = StringUtils.trimAllWhitespace(companyO.getAbbreviatedName());
        nameQuery = StringUtils.hasLength(nameQuery) ? nameQuery + "%" : nameQuery;
        logger.info("FuzzySearch nameQuery: " + nameQuery + ", phoneQuery: " + phoneQuery + ".");
        return companyMapper.queryCompanyByFuzzySearch(phoneQuery, nameQuery);
    }

    public List<CompanyCategoryO> getCompanyCategories() {
        return companyMapper.queryCompanyCategories();
    }

    public List<CompanyO>getCompaniesByCategory(int id) {
         return companyMapper.queryCompaniesByCategory(id);
    }


    public List<RelevantCompanyCategoryO> getRelevantCompanyCategories() {
        return companyMapper.queryRelevantCompanyCategories();
    }

    public List<RelevantCompanyO> getRelevantCompaniesByCategory(int id) {
        return companyMapper.queryRelevantCompaniesByCategory(id);
    }
}
