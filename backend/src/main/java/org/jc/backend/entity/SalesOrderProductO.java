package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

@Getter
@Setter
public class SalesOrderProductO {
    private int salesOrderProductID;
    private String salesOrderEntryID;

    private String skuID;
    //from w_model, w_measurement_unit, w_factory_brand
    private String newCode;
    private String oldCode;
    private String unitName;
    private String factoryCode;

    @Min(value = 1, message = "quantity smaller than one error")
    private int quantity;
    private String remark;
    private int warehouseStockID;
    private int warehouseID;
    private double taxRate;
    @DecimalMin(value = "0.0", message = "unitPriceWithoutTax smaller than zero error")
    private double unitPriceWithoutTax;
    @DecimalMin(value = "0.0", message = "unitPriceWithTax smaller than zero error")
    private double unitPriceWithTax;
    private double stockUnitPrice;
}
