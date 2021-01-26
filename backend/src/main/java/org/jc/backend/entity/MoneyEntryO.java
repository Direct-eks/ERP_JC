package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public class MoneyEntryO {
    private String moneyEntrySerial;

    @NotNull(message = "partnerCompanyID null error")
    private Integer partnerCompanyID;
    //from c_partner_company
    private String companyAbbreviatedName;
    private String companyFullName;
    private String companyPhone;

    @NotNull(message = "paymentIndication null error")
    @Pattern(regexp = "^(正常|退款)$", message = "paymentIndication value error")
    private String paymentIndication;

    @NotNull(message = "paymentMethod null error")
    @Pattern(regexp = "^(现金|转支|电汇|汇票|其他)$", message = "paymentMethod value error")
    private String paymentMethod;

    @NotNull(message = "paymentNumber null error")
    private String paymentNumber;

    @NotNull(message = "paymentAmount null error")
    @NotBlank(message = "paymentAmount blank error")
    @Pattern(regexp = "^[\\d]*?\\.?[\\d]*?$", message = "paymentAmount value error")
    private String paymentAmount;

    @NotNull(message = "bankAccountID null error")
    private Integer bankAccountID;
    // from c_bank_account
    private String bankAccountName;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "drawer null error")
    @NotBlank(message = "drawer blank error")
    private String drawer;

    @NotNull(message = "creationDate null error")
    @NotBlank(message = "creationDate blank error")
    private String creationDate;

    @NotNull(message = "paymentDate null error")
    @NotBlank(message = "paymentDate blank error")
    private String paymentDate;

    private String checkoutSerial;

    @NotNull(message = "departmentID null error")
    private Integer departmentID;
    // from e_department
    private String departmentName;

    private int isModified;
}
