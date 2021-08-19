package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jc.backend.entity.FeeEntryDetailO;

import javax.validation.Valid;
import javax.validation.constraints.*;
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

    @NotNull(message = "sourceAccountId null error")
    private Integer sourceAccountId;

    @NotNull(message = "destinationAccountId null error")
    private Integer destinationAccountId;

    @NotNull(message = "amount null error")
    @NotBlank(message = "amount blank error")
    private String amount;

    @NotNull(message = "number null error")
    private String number;

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

    @NotEmpty(message = "feeDetails empty error")
    @Valid
    private List<FeeEntryDetailO> feeDetails;
}
