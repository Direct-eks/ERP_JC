package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoneyEntryO {
    private String moneyEntrySerial;

    private int partnerCompanyID;
    //from c_partner_company
    private String companyAbbreviatedName;

    private String invoiceIndication;
    private String paymentMethod;
    private String paymentNumber;
    private double paymentAmount;

    private int bankAccountID;
    // from c_bank_account
    private String bankAccountName;

    private String remark;
    private String drawer;
    private String creationDate;
    private String paymentDate;
    private String checkoutSerial;

    private int departmentID;
    // from e_department
    private String departmentName;

    private int isModified;
}
