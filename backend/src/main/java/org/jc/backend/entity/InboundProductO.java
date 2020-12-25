package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

@Getter
@Setter
public class InboundProductO {
    private int inboundProductID;
    private String inboundEntryID;
    private int skuID;
    // from w_model, w_measurement_unit, w_factory_brand
    private String newModelCode;
    private String oldModelCode;
    private String unitName;
    private String factoryCode;

    @Min(value = 1, message = "quantity smaller than one error")
    private int quantity;
    private int stockQuantity;
    private String remark;
    private int warehouseStockID;
    private int warehouseID;
    private double taxRate;
    @DecimalMin(value = "0.0", message = "unitPriceWithoutTax smaller than zero error")
    private double unitPriceWithoutTax;
    @DecimalMin(value = "0.0", message = "unitPriceWithTax smaller than zero error")
    private double unitPriceWithTax;
    private double stockUnitPrice;
    private int returnStatus;
    private String checkoutSerial;
    private String invoiceSerial;
}
