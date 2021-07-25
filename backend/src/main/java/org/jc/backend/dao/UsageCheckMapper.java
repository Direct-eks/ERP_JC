package org.jc.backend.dao;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

@Indexed
@Mapper
@Repository
public interface UsageCheckMapper {
    Integer findPartnerCompanyIDInEntries(String table, int id);
    Integer findPartnerCompanyCategoryIDInCompanies(int id);
    Integer findRelevantCompanyIDInEntries(String table, int id);
    Integer findRelevantCompanyCategoryIDInCompanies(int id);
    Integer findWarehouseIDInEntries(String table, int id);
    Integer findDepartmentIDInEntries(String table, int id);
    Integer findBankAccountIDInEntries(String table, int id);
    Integer findUnitIDInModels(int id);
    Integer findModelIDInSkus(int id);
    Integer findModelCategoryIDInModels(int id);
    Integer findSkuIDInProducts(String table, int id);
    Integer findWarehouseStockIDInProducts(String table, int id);
}
