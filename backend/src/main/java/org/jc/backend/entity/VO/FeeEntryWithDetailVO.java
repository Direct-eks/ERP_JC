package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jc.backend.config.validation.DecimalValidation;
import org.jc.backend.entity.FeeEntryDetailO;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class FeeEntryWithDetailVO {
    private String feeEntryID;

    @NotNull(message = "entryDate null error")
    @NotBlank(message = "entryDate blank error")
    private String entryDate;

    @NotNull(message = "creationDate null error")
    @NotBlank(message = "creationDate blank error")
    private String creationDate;

    @NotNull(message = "drawer null error")
    @NotBlank(message = "drawer blank error")
    private String drawer;

    private Integer sourceAccountID;
    private String sourceAccountName;

    private Integer destinationAccountID;
    private String destinationAccountName;

    @NotNull(message = "amount null error")
    @DecimalValidation(type = DecimalValidation.ValidationTypeEnum.DECIMAL_2,
            message = "amount value error")
    private String amount;

    @NotNull(message = "number null error")
    @NotBlank(message = "number blank error")
    private String number;

    @NotNull(message = "departmentID null error")
    @Min(value = 0, message = "部门不能为空")
    private Integer departmentID;
    private String departmentName;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "isBookKeeping null error")
    @Min(value = 0, message = "isBookKeeping min value error")
    @Max(value = 1, message = "isBookKeeping max value error")
    private Integer isBookKeeping;

    @NotNull(message = "isVerified null error")
    @Min(value = 0, message = "isVerified min value error")
    @Max(value = 1, message = "isVerified max value error")
    private Integer isVerified;

    @Valid
    private List<FeeEntryDetailO> feeDetails;

    private int isModified;
    private String inOrOut;
    private String balance;
}
