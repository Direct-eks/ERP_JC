package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class MoneyEntryO {
    private String moneyEntrySerial;

    private int partnerCompanyID;
    //from c_partner_company
    private String companyAbbreviatedName;
    private String companyFullName;
    private String companyPhone;

    @NotNull(message = "paymentIndication null error")
    @Pattern(regexp = "^(正常|退款)$", message = "paymentIndication value error")
    private String paymentIndication;

    private String paymentMethod;
    private String paymentNumber;

    @NotNull(message = "paymentAmount null error")
    @NotBlank(message = "paymentAmount blank error")
    @Pattern(regexp = "^[\\d]*?\\.?[\\d]*?$", message = "paymentAmount value error")
    private String paymentAmount;

    private int bankAccountID;
    // from c_bank_account
    private String bankAccountName;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "drawer null error")
    @NotBlank(message = "drawer blank error")
    private String drawer;

    private String creationDate;
    private String paymentDate;
    private String checkoutSerial;

    private int departmentID;
    // from e_department
    private String departmentName;

    private int isModified;
}
