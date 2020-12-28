package org.jc.backend.service;

import org.jc.backend.config.exception.GlobalException;
import org.jc.backend.entity.CompanyCategoryO;
import org.jc.backend.entity.CompanyO;
import org.jc.backend.entity.RelevantCompanyCategoryO;
import org.jc.backend.entity.RelevantCompanyO;

import java.util.List;

public interface CompanyService {
    List<CompanyO> getCompanyByFuzzySearch(String phone, String name);
    List<CompanyCategoryO> getCompanyCategories();
    List<CompanyO> getCompaniesByCategory(int id);

    List<RelevantCompanyCategoryO> getRelevantCompanyCategories();
    List<RelevantCompanyO> getRelevantCompaniesByCategory(int id);
}
