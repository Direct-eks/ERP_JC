package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class OutboundEntryCompleteO {
    private String outboundEntryID;
    @NotNull(message = "drawer null error")
    private String drawer;

    @DecimalMin(value = "0.0", message = "shippingCost smaller than zero error")
    private double shippingCost;
    @Pattern(regexp = "^(自付|代垫)$", message = "shipping cost value error")
    private String shippingCostType;
    @Min(value = 0, message = "quantity smaller than zero error")
    private int shippingQuantity;
    @NotNull(message = "shippingNumber null error")
    private String shippingNumber;
    private int shippingMethodID;
    // used for modification record only
    private String relevantCompanyName;
}
