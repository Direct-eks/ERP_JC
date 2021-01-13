package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.CompanyCategoryO;
import org.jc.backend.entity.CompanyO;
import org.jc.backend.entity.RelevantCompanyCategoryO;
import org.jc.backend.entity.RelevantCompanyO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CompanyMapper {
    List<CompanyO> queryCompanyByFuzzySearch(String phone, String name);
    List<CompanyCategoryO> queryCompanyCategories();
    List<CompanyO> queryCompaniesByCategory(int id);

    CompanyO querySelfCompany();

    List<RelevantCompanyCategoryO> queryRelevantCompanyCategories();
    List<RelevantCompanyO> queryRelevantCompaniesByCategory(int id);
}
