package org.jc.backend.service;

import org.jc.backend.entity.CompanyCategoryO;
import org.jc.backend.entity.CompanyO;
import org.jc.backend.entity.RelevantCompanyCategoryO;
import org.jc.backend.entity.RelevantCompanyO;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface CompanyService {
    List<CompanyO> getCompanyByFuzzySearch(String phone, String name);
    List<CompanyCategoryO> getCompanyCategories();
    List<CompanyO> getCompaniesByCategory(int id);

    CompanyO getSelfCompany();

    void updatePartnerCompanyWithArea(int areaID, ListUpdateVO<CompanyO> updateVO);

    List<RelevantCompanyCategoryO> getRelevantCompanyCategories();
    List<RelevantCompanyO> getRelevantCompaniesByCategory(int id);

    void updateRelevantCompanyWithCategory(int categoryID, ListUpdateVO<RelevantCompanyO> updateVO);
}
