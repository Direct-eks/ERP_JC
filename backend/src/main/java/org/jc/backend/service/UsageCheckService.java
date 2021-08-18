package org.jc.backend.service;

import org.springframework.stereotype.Indexed;

@Indexed
public interface UsageCheckService {
    boolean isPartnerCompanyIDInUse(int companyID);
    boolean isPartnerCompanyCategoryIDInUse(int categoryID);
    boolean isRelevantCompanyIDInUse(int companyID);
    boolean isRelevantCompanyCategoryIDInUse(int categoryID);
    boolean isWarehouseIDInUse(int warehouseID);
    boolean isDepartmentIDInUse(int departmentID);
    boolean isBankAccountIDInUse(int bankID);
    boolean isBrandIDInUse(int brandID);
    boolean isUnitIDInUse(int unitID);
    boolean isModelIDInUse(int modelID);
    boolean isModelCategoryIDInUse(int categoryID);
    boolean isSkuIDInUse(int skuID);
    boolean isWarehouseStockIDInUse(int warehouseStockID);
    boolean isFeeCategoryIDInUse(int categoryID);
}
