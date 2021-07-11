package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public class SkuFullO {
    @NotNull(message = "skuID null error")
    private Integer skuID;

    @NotNull(message = "factoryBrandID null error")
    private Integer factoryBrandID;
    // from w_factory_brand
    private String factoryCode;

    @NotNull(message = "modelID null error")
    private Integer modelID;
    // from w_model
    private String code;

    @NotNull(message = "unitID null error")
    private Integer unitID;
    // from w_measurement_unit
    private String unitName;

    private String remark;

    private int supplierID;
    // from c_partner_company
    private String companyAbbreviatedName;
    private String companyFullName;
    private String companyPhone;
    private String companyRemark;

    @NotNull(message = "recordFactoryStock null error")
    private String recordFactoryStock;

    @NotNull(message = "factoryPriceWithoutTax null error")
    @Pattern(regexp = "^(([1-9][\\d]*?\\.\\d+)|([1-9][\\d]*)|(0\\.[\\d]+)|(0))$",
            message = "factoryPriceWithoutTax value error")
    private String factoryPriceWithoutTax;

    @NotNull(message = "factoryPriceWithTax null error")
    @Pattern(regexp = "^(([1-9][\\d]*?\\.\\d+)|([1-9][\\d]*)|(0\\.[\\d]+)|(0))$",
            message = "factoryPriceWithTax value error")
    private String factoryPriceWithTax;

    @NotNull(message = "settlementPriceWithoutTax null error")
    @Pattern(regexp = "^(([1-9][\\d]*?\\.\\d+)|([1-9][\\d]*)|(0\\.[\\d]+)|(0))$",
            message = "settlementPriceWithoutTax value error")
    private String settlementPriceWithoutTax;

    @NotNull(message = "priceBaseReference null error")
    @Pattern(regexp = "^(无税厂价|含税厂价|无税结算价|)$", message = "priceBaseReference value error")
    private String priceBaseReference;

    @NotNull(message = "wholesalePriceDiscount null error")
    @Pattern(regexp = "^([1-9][\\d]*)|(0)$",
            message = "wholesalePriceDiscount value error")
    private String wholesalePriceDiscount;

    @NotNull(message = "wholesalePrice null error")
    @Pattern(regexp = "^(([1-9][\\d]*?\\.\\d+)|([1-9][\\d]*)|(0\\.[\\d]+)|(0))$",
            message = "wholesalePrice value error")
    private String wholesalePrice;

    @NotNull(message = "retailPriceDiscount null error")
    @Pattern(regexp = "^([1-9][\\d]*)|(0)$",
            message = "retailPriceDiscount value error")
    private String retailPriceDiscount;

    @NotNull(message = "retailPrice null error")
    @Pattern(regexp = "^(([1-9][\\d]*?\\.\\d+)|([1-9][\\d]*)|(0\\.[\\d]+)|(0))$",
            message = "retailPrice value error")
    private String retailPrice;


    @NotNull(message = "stockLowerLimit null error")
    private Integer stockLowerLimit;
    @NotNull(message = "stockUpperLimit null error")
    private Integer stockUpperLimit;

    private int stockQuantity;
    private String stockUnitPrice;
}
