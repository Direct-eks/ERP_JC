package org.jc.backend.service;

import org.springframework.stereotype.Indexed;

@Indexed
public interface UsageCheckService {
    boolean isPartnerCompanyIDInUse(int companyID);
    boolean isRelevantCompanyIDInUse(int companyID);
    boolean isWarehouseIDInUse(int warehouseID);
    boolean isDepartmentIDInUse(int departmentID);
    boolean isBankAccountIDInUse(int bankID);
    boolean isUnitIDInUse(int unitID);
    boolean isModelIDInUse(int modelID);
    boolean isSkuIDInUse(int skuID);
    boolean isWarehouseStockIDInUse(int warehouseStockID);
}
