package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

@Getter
@Setter
public class QuotaProductO {
    private int quotaProductID;
    private String quotaEntryID;

    private int skuID;
    // from w_model, w_measurement_unit, w_factory_brand
    private String newCode;
    private String oldCode;
    private String unitName;
    private String factoryCode;

    @Min(value = 1, message = "quantity smaller than one error")
    private int quantity;
    private String remark;
    private double taxRate;
    @DecimalMin(value = "0.0", message = "unitPriceWithoutTax smaller than zero error")
    private double unitPriceWithoutTax;
    @DecimalMin(value = "0.0", message = "unitPriceWithTax smaller than zero error")
    private double unitPriceWithTax;
}
