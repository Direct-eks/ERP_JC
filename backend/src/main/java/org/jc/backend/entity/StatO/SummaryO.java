package org.jc.backend.entity.StatO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SummaryO {
    private String entryID;
    private String entryDate;
    private String invoiceType;
    private int taxRate;
    private String drawer;

    private int partnerCompanyID;
    // from c_partner_company
    private String companyAbbreviatedName;

    private int departmentID;
    private int warehouseID;

    private String remark;
    private String classification;
    private String shippingCost;
    private String shippingCostType;
    private int shippingQuantity;
    private String shippingNumber;

    private int shippingMethodID;
    // from c_relevant_company
    private String relevantCompanyName;

    // product
    private int productID;
    private Integer skuID;
    // from w_model, w_measurement_unit, w_factory_brand
    private String code;
    private String unitName;
    private String factoryCode;

    private Integer warehouseStockID;
    private String unitPriceWithoutTax;
    private String unitPriceWithTax;
    private Integer quantity;
    private String totalPrice;

}
