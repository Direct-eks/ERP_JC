package org.jc.backend.service;

import org.jc.backend.entity.ModificationO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface ModificationRecordService {
    void insertRecord(String serial, String content);
    void insertRecord(String serial, StringBuilder content);
    void insertRecord(String category, int id, String content);
    void insertRecord(String category, int id, StringBuilder content);
    List<ModificationO> getEntryRecordsBySerial(String serial);
    List<ModificationO> getMiscRecordsByCategoryAndID(String category, int id);

    String COMPANY_CATEGORY = "partnerCompanyCategory";
    String COMPANY = "partnerCompany";
    String R_COMPANY_CATEGORY = "relevantCompanyCategory";
    String R_COMPANY = "relevantCompany";
    String DEPARTMENT = "department";
    String BANK_ACCOUNT = "bankAccount";
    String FEE_CATEGORY = "feeCategory";
    String BRAND = "factoryBrand";
    String UNIT = "measurementUnit";
    String MODEL = "model";
    String MODEL_CATEGORY = "modelCategory";
    String SKU = "sku";
    String STORAGE = "storagePlace";
    String SUPPLIER = "supplier";
    String SUPPLIER_RESOURCE = "supplierResource";
    String WAREHOUSE = "warehouse";
    String WAREHOUSE_STOCK = "warehouseStock";

    String DELETION_MSG = "删除：";
}
