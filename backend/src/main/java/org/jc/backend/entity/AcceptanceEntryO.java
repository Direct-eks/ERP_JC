package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jc.backend.config.validation.DecimalValidation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public class AcceptanceEntryO {
    private String acceptanceEntrySerial;

    @NotNull(message = "partnerCompanyID null error")
    @Min(value = 0, message = "partnerCompanyID value error")
    private Integer partnerCompanyID;
    // from c_partner_company
    private String companyAbbreviatedName;

    @NotNull(message = "entryDate null error")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "办理日期错误")
    private String entryDate;

    @NotNull(message = "creationDate null error")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "creationDate value error")
    private String creationDate;

    @NotNull(message = "departmentID null error")
    @Min(value = 0, message = "部门错误")
    private Integer departmentID;
    // from e_department
    private String departmentName;

    @NotNull(message = "source null error")
    private String source;

    @NotNull(message = "bankAccountID null error")
    private Integer bankAccountID;
    // from c_bank_account
    private String bankAccountName;

    @NotNull(message = "sourceSerial null error")
    private String sourceSerial;

    @NotNull(message = "amount null error")
    @DecimalValidation(type = DecimalValidation.ValidationTypeEnum.DECIMAL_2,
            message = "总金额值错误")
    private String amount;

    @NotNull(message = "number null error")
    @NotBlank(message = "汇票号码不能为空")
    private String number;

    @NotNull(message = "issueDate null error")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "出票日期错误")
    private String issueDate;
    @NotNull(message = "expirationDate null error")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "到期日期错误")
    private String expirationDate;

    @NotNull(message = "type null error")
    @Pattern(regexp = "^(银行|商业)$", message = "type value error")
    private String type;

    @NotNull(message = "drawer null error")
    @NotBlank(message = "drawer blank error")
    private String drawer;

    @NotNull(message = "remark null error")
    private String remark;

    private String classification;
    private int status;
}
