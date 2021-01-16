package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseStockO {
    private int warehouseStockID;
    private int skuID;
    private int warehouseID;
    //from w_warehouse
    private String warehouseName;

    private int stockQuantity;
    private double stockUnitPriceWithoutTax;
    private double stockUnitPriceWithTax;
    private String remark;
    private double factoryPriceWithoutTax;
    private double factoryPriceWithTax;
    private double settlementPriceWithoutTax;
    private String priceBaseReference;
    private double wholesalePriceDiscount;
    private double wholesalePrice;
    private double retailPriceDiscount;
    private double retailPrice;
    private int stockLowerLimit;
    private int stockUpperLimit;
    private int storagePlaceID;
}
