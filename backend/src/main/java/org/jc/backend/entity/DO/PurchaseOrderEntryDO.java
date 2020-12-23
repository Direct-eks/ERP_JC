package org.jc.backend.entity.DO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PurchaseOrderEntryDO {
    private String purchaseOrderEntryID;
    private String serial;
    private Date entryDate;
    private Date creationDate;
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
}
