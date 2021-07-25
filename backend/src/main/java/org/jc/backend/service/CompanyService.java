package org.jc.backend.service;

import org.jc.backend.entity.CompanyAreaO;
import org.jc.backend.entity.CompanyO;
import org.jc.backend.entity.RelevantCompanyCategoryO;
import org.jc.backend.entity.RelevantCompanyO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface CompanyService {
    List<CompanyO> getCompanyByFuzzySearch(String phone, String name);
    List<CompanyAreaO> getCompanyAreas();
    List<CompanyO> getCompaniesByAreaID(int id);
    CompanyO getCompanyByID(int id);

    CompanyO getSelfCompany();

    void updateCompanyAreas(List<CompanyAreaO> updateVO);
    void updatePartnerCompanyWithArea(int areaID, List<CompanyO> updateVO);

    List<RelevantCompanyCategoryO> getRelevantCompanyCategories();
    List<RelevantCompanyO> getRelevantCompaniesByCategory(int id);

    void updateRelevantCompanyCategories(List<RelevantCompanyCategoryO> updateVO);
    void updateRelevantCompanyWithCategory(int categoryID, List<RelevantCompanyO> updateVO);
}
