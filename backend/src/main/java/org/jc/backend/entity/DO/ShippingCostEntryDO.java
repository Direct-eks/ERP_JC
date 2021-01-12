package org.jc.backend.entity.DO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingCostEntryDO {
    private String shippingCostEntrySerial;

    private int partnerCompanyID;
    //from c_partner_company
    private String companyAbbreviatedName;
    private String companyFullName;
    private String companyPhone;

    private int isTaxDeduction;
    private String invoiceNumber;
    private double totalAmount;
    private double invoiceAmount;
    private String shippingPaymentType;
    private String remark;
    private String drawer;
    private String creationDate;
    private String checkoutDate;
    private int inOrOut;
    private String invoiceDate;
    private int isModified;
}
