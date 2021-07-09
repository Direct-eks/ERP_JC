package org.jc.backend.entity.StatO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StockStatO {
    private int warehouseStockID;
    private int skuID;

    private String categoryCode;
    // from w_factory_brand
    private String factoryCode;
    // from w_model
    private String code;
    // from w_measurement_unit
    private String unitName;

    private int warehouseID;
    //from w_warehouse
    private String warehouseName;

    private int stockQuantity;
    private String stockUnitPrice;
}
