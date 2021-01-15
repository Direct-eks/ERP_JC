package org.jc.backend.entity.StatO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntryProductVO {
    private String entryID;

    private int inboundProductID;
    private String inboundEntryID;
    private int outboundProductID;
    private String outboundEntryID;
    private int skuID;
    // from w_model, w_measurement_unit, w_factory_brand
    private String newCode;
    private String oldCode;
    private String unitName;
    private String factoryCode;

    private int quantity;
    private int stockQuantity;
    private String remark;
    private int warehouseStockID;
    private int warehouseID;
    private double taxRate;
    private double unitPriceWithoutTax;
    private double unitPriceWithTax;
    private double stockUnitPrice;
    private int returnStatus;
    private String checkoutSerial;
    private String invoiceSerial;
}
