package org.jc.backend.service;

import org.jc.backend.config.exception.GlobalException;
import org.jc.backend.entity.CompanyCategoryO;
import org.jc.backend.entity.CompanyO;

import java.util.List;

public interface CompanyService {
    List<CompanyO> getCompanyByFuzzySearch(CompanyO companyO);
    List<CompanyCategoryO> getCompanyCategories();
    List<CompanyO>getCompaniesByCategory(int id);
}
