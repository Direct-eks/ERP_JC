package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.CompanyCategoryO;
import org.jc.backend.entity.CompanyO;
import org.jc.backend.entity.RelevantCompanyCategoryO;
import org.jc.backend.entity.RelevantCompanyO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface CompanyMapper {
    List<CompanyCategoryO> queryCompanyCategories();
    void insertPartnerCompanyArea(CompanyCategoryO categoryO);
    void updatePartnerCompanyArea(CompanyCategoryO categoryO);

    List<CompanyO> queryCompanyByFuzzySearch(String phone, String name);
    List<CompanyO> queryCompaniesByCategory(int id);
    CompanyO querySelfCompany();
    void insertCompany(CompanyO companyO);
    void updateCompany(CompanyO companyO);

    List<RelevantCompanyO> queryRelevantCompaniesByCategory(int id);
    void insertRelevantCompanyCategory(RelevantCompanyCategoryO categoryO);
    void updateRelevantCompanyCategory(RelevantCompanyCategoryO categoryO);

    List<RelevantCompanyCategoryO> queryRelevantCompanyCategories();

    void insertRelevantCompany(RelevantCompanyO companyO);
    void updateRelevantCompany(RelevantCompanyO companyO);
}
