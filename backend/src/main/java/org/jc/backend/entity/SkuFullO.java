package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkuFullO {
    private int skuID;
    private int factoryBrandID;

    // from w_factory_brand
    private String factoryCode;
    private int modelID;
    // from w_model
    private String newCode;
    private String oldCode;
    private int unitID;
    // from w_measurement_unit
    private String unitName;

    private int stockQuantity;
    private String remark;

    private int supplierID;
    // from c_partner_company
    private String companyAbbreviatedName;
    private String companyFullName;
    private String companyPhone;
    private String companyRemark;

    private int recordFactoryStock;
    private String factoryPriceWithoutTax;
    private String factoryPriceWithTax;
    private String settlementPriceWithoutTax;
    private String priceBaseReference;
    private String wholesalePriceDiscount;
    private String wholesalePrice;
    private String retailPriceDiscount;
    private String retailPrice;
    private int stockLowerLimit;
    private int stockUpperLimit;
}
