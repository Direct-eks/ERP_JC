package org.jc.backend.entity.DO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutEntryDO {
    private String checkoutEntrySerial;

    private int partnerCompanyID;
    // from c_partner_company
    private String companyAbbreviatedName;
    private String companyFullName;
    private String companyPhone;
    private String companyRemark;

    private String invoiceType;
    private String paymentMethod;
    private String paymentNumber;
    private String paymentAmount;

    private int bankAccountID;
    //from c_bank_account
    private String bankAccountName;

    private String totalAmount;
    private int isRounded;
    private String roundedAmount;
    private String debt;
    private String serviceFee;
    private String remark;
    private String drawer;
    private String creationDate;
    private String checkoutDate;
    private String moneyEntrySerial;
    private String returnSerial;

    private int departmentID;
    //from e_department
    private String departmentName;

    private int isVerified;
    private int isModified;
}
