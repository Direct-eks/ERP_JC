package org.jc.backend.entity.StatO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CheckoutSummaryO {
    private String checkoutEntrySerial;

    private int partnerCompanyID;
    // from c_partner_company
    private String companyAbbreviatedName;

    private String invoiceType;

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
