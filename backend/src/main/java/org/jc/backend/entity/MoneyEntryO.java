package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoneyEntryO {
    private String moneyEntrySerial;
    private int partnerCompanyID;
    private String invoiceIndication;
    private String paymentMethod;
    private String paymentNumber;
    private double paymentAmount;
    private int bankAccountID;
    private String remark;
    private String drawer;
    private String creationDate;
    private String paymentDate;
    private String checkoutSerial;
    private int departmentID;
    private int isModified;
}
