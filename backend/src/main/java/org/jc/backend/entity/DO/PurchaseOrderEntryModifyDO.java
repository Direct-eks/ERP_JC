package org.jc.backend.entity.DO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PurchaseOrderEntryModifyDO {
    private String purchaseOrderEntryID;
    private double totalCost;
    private String invoiceType;
    private String executionStatus;
    private String drawer;
    private int departmentID;
    private int warehouseID;
    private String remark;

    private boolean totalCostChange;
    private boolean invoiceTypeChange;
    private boolean executionStatusChange;
    // drawer is not updated but only queried
    private boolean departmentIDChange;
    private boolean warehouseIDChange;
    private boolean remarkChange;

    //only used for modification record
    private String departmentName;
    private String warehouseName;
}
