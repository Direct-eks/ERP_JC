package org.jc.backend.entity.DO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesOrderEntryDO {
    private String salesOrderEntryID;
    private String shipmentDate;
    private String creationDate;
    private String totalAmount;
    private String invoiceType;
    private String executionStatus;
    private String drawer;

    private int partnerCompanyID;
    //from c_partner_company
    private String companyAbbreviatedName;
    private String companyFullName;
    private String companyPhone;

    private int departmentID;
    //from e_department
    private String departmentName;

    private int warehouseID;
    //from w_warehouse
    private String warehouseName;

    private String remark;
    private int isModified;
}
