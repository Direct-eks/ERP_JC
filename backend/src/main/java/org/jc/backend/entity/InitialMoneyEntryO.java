package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jc.backend.config.validation.DecimalValidation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public class InitialMoneyEntryO {
    private String initialMoneyEntrySerial;

    @NotNull(message = "partnerCompanyID null error")
    @Min(value = 0, message = "partnerCompanyID value error")
    private Integer partnerCompanyID;
    // from c_partner_company
    private String abbreviatedName;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "drawer null error")
    private String drawer;

    @NotNull(message = "entryDate null error")
    private String entryDate;

    @NotNull(message = "creationDate null error")
    private String creationDate;

    private int isModified;

    @NotNull(message = "debitOrCredit null error")
    @Pattern(regexp = "^[借贷]$", message = "debitOrCredit value error")
    private String debitOrCredit;

    @NotNull(message = "balance null error")
    @DecimalValidation(type = DecimalValidation.ValidationTypeEnum.DECIMAL_8,
            message = "数值错误")
    private String balance;
}
