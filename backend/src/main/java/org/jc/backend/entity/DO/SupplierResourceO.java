package org.jc.backend.entity.DO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public class SupplierResourceO {

    @NotNull(message = "supplierID null error")
    private Integer supplierID;

    @NotNull(message = "skuID null error")
    private Integer skuID;

    @NotNull(message = "quantityRecord null error")
    private String quantityRecord;

    @NotNull(message = "factoryPriceWithoutTax null error")
    @NotBlank(message = "factoryPriceWithoutTax blank error")
    @Pattern(regexp = "^[\\d]*?\\.?[\\d]*?$", message = "factoryPriceWithoutTax value error")
    private String factoryPriceWithoutTax;

    @NotNull(message = "factoryPriceWithTax null error")
    @NotBlank(message = "factoryPriceWithTax blank error")
    @Pattern(regexp = "^[\\d]*?\\.?[\\d]*?$", message = "factoryPriceWithTax value error")
    private String factoryPriceWithTax;

    @Min(value = 0, message = "floatDownPrice smaller than zero error")
    private int floatDownPrice;

    @NotNull(message = "settlementPriceWithoutTax null error")
    @NotBlank(message = "settlementPriceWithoutTax blank error")
    @Pattern(regexp = "^[\\d]*?\\.?[\\d]*?$", message = "settlementPriceWithoutTax value error")
    private String settlementPriceWithoutTax;

    @NotNull(message = "numberOfBoxes null error")
    private String numberOfBoxes;

    private String lastSettlementPrice;

    @NotNull(message = "remark null error")
    private String remark;
}
