package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jc.backend.config.validation.DecimalValidation;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class BankAccountO implements ObjectComparison<BankAccountO> {

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

    @Override
    public BankAccountO getOldObject(List<BankAccountO> oldObjectList) {
        for (var item : oldObjectList) {
            if (this.bankAccountID.equals(item.bankAccountID)) {
                return item;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public boolean formModificationRecord(BankAccountO oldAccountO, StringBuilder record) {
        if (oldAccountO == this) return true;
        boolean bool = false;

        if (!this.name.equals(oldAccountO.getName())) {
            bool = true;
            record.append(String.format("名称：%s -> %s; ", oldAccountO.getName(), this.name));
        }
        if (!this.accountNumber.equals(oldAccountO.getAccountNumber())) {
            bool = true;
            record.append(String.format("账号：%s -> %s; ", oldAccountO.getAccountNumber(), this.accountNumber));
        }
        if (!this.remark.equals(oldAccountO.getRemark())) {
            bool = true;
            record.append(String.format("说明：%s -> %s; ", oldAccountO.getRemark(), this.remark));
        }
        if (!this.isVisible.equals(oldAccountO.getIsVisible())) {
            bool = true;
            record.append(String.format("可见：%s -> %s; ", oldAccountO.getIsVisible(), this.isVisible));
        }
        if (!this.inUse.equals(oldAccountO.getInUse())) {
            bool = true;
            record.append(String.format("启用：%s -> %s; ", oldAccountO.getInUse(), this.inUse));
        }
        if (!this.initialBalance.equals(oldAccountO.getInitialBalance())) {
            bool = true;
            record.append(String.format("期初：%s -> %s; ", oldAccountO.getInitialBalance(), this.initialBalance));
        }

        return bool;
    }
}
