package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jc.backend.config.validation.DecimalValidation;
import org.jc.backend.entity.WarehouseProductO;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@ToString
public class WarehouseEntryWithProductsVO {
    private String warehouseEntryID;

    @NotNull(message = "entryDate null error")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日期错误")
    private String entryDate;

    @NotNull(message = "creationDate null error")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "creation date error")
    private String creationDate;

    @NotNull(message = "totalAmount null error")
    @DecimalValidation(type = DecimalValidation.ValidationTypeEnum.DECIMAL_8,
            message = "总金额校验错误")
    private String totalAmount;

    @NotNull(message = "drawer null error")
    @NotBlank(message = "drawer blank error")
    private String drawer;

    @NotNull(message = "departmentID null error")
    @Min(value = 0, message = "部门错误")
    private Integer departmentID;
    // from e_department
    private String departmentName;

    @NotNull(message = "warehouseID null error")
    @Min(value = 0, message = "仓库错误")
    private Integer warehouseID;
    // from w_warehouse
    private String warehouseName;

    @NotNull(message = "remark null error")
    private String remark;

    private String relatedEntrySerial = "";

    private String classification;

    @Valid
    @NotEmpty
    private List<WarehouseProductO> entryProducts;
}
