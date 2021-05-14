package org.jc.backend.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SupplierResourceO {

    @NotNull(message = "supplierID null error")
    private Integer supplierID;
    // from c_partner_company
    private String supplierAbbreviatedName;
    private String supplierFullName;
    private String supplierPhone;

    @NotNull(message = "skuID null error")
    private Integer skuID;
    // from w_model
    private Integer modelID;
    private String code;

    private Integer factoryBrandID;
    // from w_factory_brand
    private String factoryCode;

    private Integer unitID;
    // from w_measurement_unit
    private String unitName;

    @NotNull(message = "quantityRecord null error")
    private String quantityRecord;

    @NotNull(message = "factoryPriceWithoutTax null error")
    @Pattern(regexp = "^(([1-9][\\d]*?\\.\\d+)|([1-9][\\d]*)|(0\\.[\\d]+)|(0))$",
            message = "factoryPriceWithoutTax value error")
    private String factoryPriceWithoutTax;

    @NotNull(message = "factoryPriceWithTax null error")
    @Pattern(regexp = "^(([1-9][\\d]*?\\.\\d+)|([1-9][\\d]*)|(0\\.[\\d]+)|(0))$",
            message = "factoryPriceWithTax value error")
    private String factoryPriceWithTax;

    @Min(value = 0, message = "floatDownRate smaller than zero error")
    private int floatDownRate;

    @NotNull(message = "settlementPriceWithoutTax null error")
    @Pattern(regexp = "^(([1-9][\\d]*?\\.\\d+)|([1-9][\\d]*)|(0\\.[\\d]+)|(0))$",
            message = "settlementPriceWithoutTax value error")
    private String settlementPriceWithoutTax;

    @NotNull(message = "numberOfBoxes null error")
    private String numberOfBoxes;

    private String lastSettlementPrice;

    @NotNull(message = "remark null error")
    private String remark;
}
