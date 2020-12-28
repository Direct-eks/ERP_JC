package org.jc.backend.entity.DO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseOrderEntryDO {
    private String purchaseOrderEntryID;
    private String entryDate;
    private String creationDate;
    private double totalCost;
    private String invoiceType;
    private String executionStatus;
    private String drawer;

    private int partnerCompanyID;
    // from c_partner_company
    private String companyAbbreviatedName;
    private String companyPhone;
    private String companyFullName;

    private int departmentID;
    // from c_department
    private String departmentName;

    private int warehouseID;
    // from w_warehouse
    private String warehouseName;

    private String remark;
    private int isModified;
}
