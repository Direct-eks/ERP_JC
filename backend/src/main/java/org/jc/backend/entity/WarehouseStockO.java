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
    private String stockUnitPriceWithoutTax;
    private String remark;
    private int storagePlaceID;
    private int initialStockQuantity;
    private String initialStockUnitPrice;
}
