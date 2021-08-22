package org.jc.backend.service.Impl;

import org.jc.backend.dao.UsageCheckMapper;
import org.jc.backend.service.UsageCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.ArrayUtils.addAll;

@Service
public class UsageCheckServiceImpl implements UsageCheckService {

    private static final Logger logger = LoggerFactory.getLogger(UsageCheckServiceImpl.class);

    private final UsageCheckMapper usageCheckMapper;

    public UsageCheckServiceImpl(UsageCheckMapper usageCheckMapper) {
        this.usageCheckMapper = usageCheckMapper;
    }

    static final String[] eTables1 = {
            "e_inbound_entry", "e_outbound_entry"
    };
    static final String[] pTables1 = {
            "e_inbound_product", "e_outbound_product"
    };
    static final String[] eTables2 = {
            "e_purchase_order_entry","e_quote_entry", "e_sales_order_entry"
    };
    static final String[] pTables2 = {
            "e_purchase_order_product", "e_sales_order_product", "e_quote_product"
    };
    static final String[] mTables = {
            "m_checkout_entry", "m_initial_money_entry", "m_invoice_entry",
            "m_money_entry", "m_shipping_cost_entry", "m_acceptance_entry"
    };
    static final String[] wTables = {
            "p_warehouse_in_entry", "p_warehouse_out_entry"
    };
    static final String[] wProducts = {
            "p_warehouse_in_product", "p_warehouse_out_product"
    };
    static final String[] warehouseStock = {
            "w_warehouse_stock"
    };

    /* ------------------------------ SERVICE ------------------------------ */

    @Override
    public boolean isPartnerCompanyIDInUse(int companyID) {
        for (var table : addAll(eTables1, addAll(eTables2, mTables))) {
             if (usageCheckMapper.findPartnerCompanyIDInEntries(table, companyID) != null) {
                 logger.info("companyID {} in use in table: {}", companyID, table);
                 return true;
             }
        }
        logger.info("companyID {} not in use", companyID);
        return false;
    }

    @Override
    public boolean isPartnerCompanyCategoryIDInUse(int categoryID) {
        if (usageCheckMapper.findPartnerCompanyCategoryIDInCompanies(categoryID) != null) {
            logger.info("partner categoryID {} in use", categoryID);
            return true;
        }
        logger.info("partner categoryID {} not in use", categoryID);
        return false;
    }

    @Override
    public boolean isRelevantCompanyIDInUse(int companyID) {
        for (var table : eTables1) {
            if (usageCheckMapper.findRelevantCompanyIDInEntries(table, companyID) != null) {
                logger.info("relevant companyID {} in use in table: {}", companyID, table);
                return true;
            }
        }
        logger.info("relevant companyID {} not in use", companyID);
        return false;
    }

    @Override
    public boolean isRelevantCompanyCategoryIDInUse(int categoryID) {
        if (usageCheckMapper.findRelevantCompanyCategoryIDInCompanies(categoryID) != null) {
            logger.info("relevant company categoryID {} in use", categoryID);
            return true;
        }
        logger.info("relevant company categoryID {} not in use", categoryID);
        return false;
    }

    @Override
    public boolean isWarehouseIDInUse(int warehouseID) {
        for (var table : addAll(eTables1, addAll(eTables2, addAll(pTables1,
                addAll(pTables2, addAll(wTables, addAll(wProducts, warehouseStock))))))) {
            if (table.equals("e_quote_entry") || table.equals("e_quote_product")) continue;
            if (usageCheckMapper.findWarehouseIDInEntries(table, warehouseID) != null) {
                logger.info("warehouseID {} in use in table: {}", warehouseID, table);
                return true;
            }
        }
        logger.info("warehouseID {} not in use", warehouseID);
        return false;
    }

    @Override
    public boolean isDepartmentIDInUse(int departmentID) {
        for (var table : addAll(eTables1, addAll(eTables2, addAll(mTables, wTables)))) {
            if (!table.equals("m_checkout_entry") && !table.equals("m_acceptance_entry")) continue;
            if (usageCheckMapper.findDepartmentIDInEntries(table, departmentID) != null) {
                logger.info("departmentID {} in use in table: {}", departmentID, table);
                return true;
            }
        }
        logger.info("departmentID {} not in use", departmentID);
        return false;
    }

    @Override
    public boolean isBankAccountIDInUse(int bankAccountID) {
        for (var table : mTables) {
            if (!table.equals("m_checkout_entry") && !table.equals("m_money_entry") &&
                    !table.equals("m_acceptance_entry")) continue;
            if (usageCheckMapper.findBankAccountIDInEntries(table, bankAccountID) != null) {
                logger.info("bankAccountID {} in use in table: {}", bankAccountID, table);
                return true;
            }
        }
        if (usageCheckMapper.findBankAccountIDInFeeEntries(bankAccountID) != null) {
            logger.info("bankAccountID {} in use in table f_fee_entry", bankAccountID);
            return true;
        }
        logger.info("bankAccountID {} not in use", bankAccountID);
        return false;
    }

    @Override
    public boolean isBrandIDInUse(int brandID) {
        if (usageCheckMapper.findBrandIDInSkus(brandID) != null) {
            logger.info("brandID {} in use", brandID);
            return true;
        }
        logger.info("brandID {} not in use", brandID);
        return false;
    }

    @Override
    public boolean isUnitIDInUse(int unitID) {
        if (usageCheckMapper.findUnitIDInModels(unitID) != null) {
            logger.info("unitID {} in use", unitID);
            return true;
        }
        logger.info("unitID {} not in use", unitID);
        return false;
    }

    @Override
    public boolean isModelIDInUse(int modelID) {
        if (usageCheckMapper.findModelIDInSkus(modelID) != null) {
            logger.info("modelID {} in use", modelID);
            return true;
        }
        logger.info("modelID {} not in use", modelID);
        return false;
    }

    @Override
    public boolean isModelCategoryIDInUse(int categoryID) {
        if (usageCheckMapper.findModelCategoryIDInModels(categoryID) != null) {
            logger.info("model categoryID {} in use", categoryID);
            return true;
        }
        logger.info("model categoryID {} not in use", categoryID);
        return false;
    }

    @Override
    public boolean isSkuIDInUse(int skuID) {
        for (var table : addAll(warehouseStock, addAll(pTables1, addAll(pTables2, wProducts)))) {
            if (usageCheckMapper.findSkuIDInProducts(table, skuID) != null) {
                logger.info("skuID {} in use in table: {}", skuID, table);
                return true;
            }
        }
        logger.info("skuID {} not in use", skuID);
        return false;
    }

    @Override
    public boolean isWarehouseStockIDInUse(int warehouseStockID) {
        for (var table : addAll(pTables1, addAll(pTables2, wProducts))) {
            if (table.equals("e_quote_product"))  continue;
            if (usageCheckMapper.findWarehouseStockIDInProducts(table, warehouseStockID) != null) {
                logger.info("warehouseStockID {} in use in table: {}", warehouseStockID, table);
                return true;
            }
        }
        logger.info("warehouseStockID {} not in use", warehouseStockID);
        return false;
    }

    @Override
    public boolean isFeeCategoryIDInUse(int categoryID) {
        if (usageCheckMapper.findFeeCategoryIDInEntries(categoryID) != null) {
            logger.info("fee categoryID {} in use", categoryID);
            return true;
        }
        logger.info("fee categoryID {} not in use", categoryID);
        return false;
    }
}
