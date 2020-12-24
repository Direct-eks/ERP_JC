package org.jc.backend.entity.DO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PurchaseOrderProductModifyDO {
    private int purchaseOrderProductID;
    private String purchaseOrderEntryID;
    private int quantity;
    private String remark;
    private double taxRate;
    private double unitPriceWithoutTax;
    private double unitPriceWithTax;

    //only used for modification record
    private String newCode;
    private String oldCode;
}
