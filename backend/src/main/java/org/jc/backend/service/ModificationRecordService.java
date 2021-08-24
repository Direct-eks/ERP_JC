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

    static final String COMPANY_CATEGORY = "partnerCompanyCategory";
    static final String COMPANY = "partnerCompany";
    static final String R_COMPANY_CATEGORY = "relevantCompanyCategory";
    static final String R_COMPANY = "relevantCompany";
    static final String DEPARTMENT = "department";
    static final String BANK_ACCOUNT = "bankAccount";
    static final String FEE_CATEGORY = "feeCategory";
    static final String BRAND = "factoryBrand";
    static final String UNIT = "measurementUnit";
    static final String MODEL = "model";
    static final String MODEL_CATEGORY = "modelCategory";
    static final String SKU = "sku";
    static final String STORAGE = "storagePlace";
    static final String SUPPLIER = "supplier";
    static final String SUPPLIER_RESOURCE = "supplierResource";
    static final String WAREHOUSE = "warehouse";
    static final String WAREHOUSE_STOCK = "warehouseStock";

    static final String DELETION_MSG = "删除";
}
