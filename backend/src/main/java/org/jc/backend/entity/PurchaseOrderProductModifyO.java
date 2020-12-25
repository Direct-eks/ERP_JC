package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

@Getter
@Setter
public class PurchaseOrderProductModifyO {
    private int purchaseOrderProductID;
    private String purchaseOrderEntryID;

    @Min(value = 1, message = "quantity not zero error")
    private int quantity;
    private String remark;

    private double taxRate;
    @DecimalMin(value = "0.0", message = "unitPriceWithoutTax smaller than zero error")
    private double unitPriceWithoutTax;
    @DecimalMin(value = "0.0", message = "unitPriceWithTax smaller than zero error")
    private double unitPriceWithTax;

    //only used for modification record
    private String newCode;
    private String oldCode;
}
