package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class MoneyEntryO {
    private String moneyEntrySerial;

    private int partnerCompanyID;
    //from c_partner_company
    private String companyAbbreviatedName;

    @NotNull(message = "paymentIndication null error")
    @Pattern(regexp = "^(正常|退款)$", message = "paymentIndication value error")
    private String paymentIndication;
    private String paymentMethod;
    private String paymentNumber;
    @DecimalMin(value = "0.0", message = "paymentAmount smaller than zero error")
    private double paymentAmount;

    private int bankAccountID;
    // from c_bank_account
    private String bankAccountName;

    @NotNull(message = "remark null error")
    private String remark;
    @NotNull(message = "drawer null error")
    private String drawer;
    private String creationDate;
    private String paymentDate;
    private String checkoutSerial;

    private int departmentID;
    // from e_department
    private String departmentName;

    private int isModified;
}
