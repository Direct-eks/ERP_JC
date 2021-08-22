package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jc.backend.config.validation.DecimalValidation;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class BankAccountO {

    @NotNull(message = "bankAccountID null error")
    private Integer bankAccountID;

    @NotNull(message = "name null error")
    @NotBlank(message = "名称不能为空白")
    private String name;

    @NotNull(message = "accountNumber null error")
    @NotBlank(message = "账号不能为空白")
    private String accountNumber;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "isVisible null error")
    @Min(value = 0, message = "isVisible min value error")
    @Max(value = 1, message = "isVisible max value error")
    private Integer isVisible;

    @NotNull(message = "inUse null error")
    @Min(value = 0, message = "inUse min value error")
    @Max(value = 1, message = "inUse max value error")
    private Integer inUse;

    @NotNull(message = "initialBalance null error")
    @DecimalValidation(type = DecimalValidation.ValidationTypeEnum.DECIMAL_2,
            message = "期初值格式错误")
    private String initialBalance;
}
