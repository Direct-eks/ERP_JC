package org.jc.backend.entity.DO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseOrderProductDO {
    private int purchaseOrderProductID;
    private String purchaseOrderEntryID;

    private int skuID;
    // from w_model, w_measurement_unit, w_factory_brand
    private String newModelCode;
    private String oldModelCode;
    private String unitName;
    private String factoryCode;

    private int quantity;
    private String remark;
    private int warehouseStockID;
    private int warehouseID;
    private double taxRate;
    private double unitPriceWithoutTax;
    private double unitPriceWithTax;
    private double stockUnitPrice;
}
