package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jc.backend.config.validation.DecimalValidation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class FeeEntryDetailO {
    private Integer feeDetailEntryID;
    private String feeEntryID;

    @NotNull(message = "feeCategoryID blank error")
    @Min(value = 0, message = "feeCategoryID value error")
    private Integer feeCategoryID;
    private String feeCategoryName;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "amount null error")
    @DecimalValidation(type = DecimalValidation.ValidationTypeEnum.DECIMAL_2,
            message = "amount value error")
    private String amount;
}
