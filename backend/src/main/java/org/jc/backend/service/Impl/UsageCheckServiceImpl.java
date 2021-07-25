package org.jc.backend.service.Impl;

import org.apache.commons.lang3.ArrayUtils;
import org.jc.backend.dao.UsageCheckMapper;
import org.jc.backend.service.UsageCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UsageCheckServiceImpl implements UsageCheckService {

    private static final Logger logger = LoggerFactory.getLogger(UsageCheckServiceImpl.class);

    private final UsageCheckMapper usageCheckMapper;

    public UsageCheckServiceImpl(UsageCheckMapper usageCheckMapper) {
        this.usageCheckMapper = usageCheckMapper;
    }

    static String[] eTables1 = {
            "e_inbound_entry", "e_outbound_entry"
    };
    static String[] pTables1 = {
            "e_inbound_product", "e_outbound_product"
    };
    static String[] eTables2 = {
            "e_purchase_order_entry","e_quote_entry", "e_sales_order_entry"
    };
    static String[] pTables2 = {
            "e_purchase_order_product", "e_sales_order_product", "e_quote_product"
    };
    static String[] mTables = {
            "m_checkout_entry", "m_initial_money_entry", "m_invoice_entry",
            "m_money_entry", "m_shipping_cost_entry"
    };
    static String[] wTables = {
            "p_warehouse_in_entry", "p_warehouse_out_entry"
    };
    static String[] wProducts = {
            "p_warehouse_in_product", "p_warehouse_out_product"
    };
    static String[] warehouseStock = {
            "w_warehouse_stock"
    };

    /* ------------------------------ SERVICE ------------------------------ */

    @Override
    public boolean isPartnerCompanyIDInUse(int companyID) {
        for (var table : (String[])ArrayUtils.addAll(eTables1, eTables2, mTables)) {
             if (usageCheckMapper.findPartnerCompanyIDInEntries(table, companyID) != null) {
                 return true;
             }
        }
        return false;
    }

    @Override
    public boolean isPartnerCompanyCategoryIDInUse(int categoryID) {
        return usageCheckMapper.findPartnerCompanyCategoryIDInCompanies(categoryID) != null;
    }

    @Override
    public boolean isRelevantCompanyIDInUse(int companyID) {
        for (var table : eTables1) {
            if (usageCheckMapper.findRelevantCompanyIDInEntries(table, companyID) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isRelevantCompanyCategoryIDInUse(int categoryID) {
        return usageCheckMapper.findRelevantCompanyCategoryIDInCompanies(categoryID) != null;
    }

    @Override
    public boolean isWarehouseIDInUse(int warehouseID) {
        for (var table : (String[]) ArrayUtils.addAll(
                eTables1, eTables2, pTables1, pTables2,
                wTables, wProducts, warehouseStock)) {
            if (usageCheckMapper.findWarehouseIDInEntries(table, warehouseID) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isDepartmentIDInUse(int departmentID) {
        for (var table : (String[]) ArrayUtils.addAll(
                eTables1, eTables2, mTables, wTables)) {
            if (usageCheckMapper.findWarehouseIDInEntries(table, departmentID) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isBankAccountIDInUse(int bankID) {
        for (var table : mTables) {
            if (table.equals("m_checkout_entry") || table.equals("m_money_entry")) {
                if (usageCheckMapper.findBankAccountIDInEntries(table, bankID) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isUnitIDInUse(int unitID) {
        return usageCheckMapper.findUnitIDInModels(unitID) != null;
    }

    @Override
    public boolean isModelIDInUse(int modelID) {
        return usageCheckMapper.findModelIDInSkus(modelID) != null;
    }

    @Override
    public boolean isModelCategoryIDInUse(int categoryID) {
        return usageCheckMapper.findModelCategoryIDInModels(categoryID) != null;
    }

    @Override
    public boolean isSkuIDInUse(int skuID) {
        for (var table : (String[])ArrayUtils.addAll(warehouseStock,
                pTables1, pTables2, wProducts)) {
            if (usageCheckMapper.findSkuIDInProducts(table, skuID) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isWarehouseStockIDInUse(int warehouseStockID) {
        for (var table : (String[])ArrayUtils.addAll(pTables1, pTables2, wProducts)) {
            if (!table.equals("e_quote_product")) {
                if (usageCheckMapper.findWarehouseStockIDInProducts(table, warehouseStockID) != null) {
                    return true;
                }
            }
        }
        return false;
    }
}
