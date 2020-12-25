package org.jc.backend.entity.DO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OutboundEntryDO {
    private String outboundEntryID;
    private String shipmentDate;
    private String creationDate;
    private double totalAmount;
    private String deliverMethod;
    private String invoiceType;
    private String drawer;

    private int partnerCompanyID;
    // from c_partner_company
    private String companyAbbreviatedName;
    private String companyPhone;
    private String companyFullName;

    private int departmentID;
    // from e_department
    private String departmentName;

    private int warehouseID;
    // from w_warehouse
    private String warehouseName;

    private String remark;
    private String classification;
    private double shippingCost;
    private String shippingCostType;
    private int shippingQuantity;
    private String shippingNumber;

    private int shippingMethodID;
    // from c_relevant_company
    private String relevantCompanyName;

    private String shippingCostSerial;
    private String returnDate;
    private String returnSerial;
    private int printTimes;
}
