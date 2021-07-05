package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jc.backend.config.validation.DecimalValidation;
import org.jc.backend.entity.PurchaseOrderProductO;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@ToString
public class PurchaseOrderEntryWithProductsVO {
    private String purchaseOrderEntryID;

    @NotNull(message = "entryDate null error")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "采购日期错误")
    private String entryDate;

    @NotNull(message = "creationDate null error")
    @NotBlank(message = "creationDate blank error")
    private String creationDate;

    @NotNull(message = "totalCost null error")
    @DecimalValidation(type = DecimalValidation.ValidationTypeEnum.DECIMAL_8,
            message = "总金额校验错误")
    private String totalCost;

    @NotNull(message = "invoiceType null error")
    @Pattern(regexp = "^(增值税票|普票|收据)$", message = "invoiceType value error")
    private String invoiceType;

    @NotNull(message = "taxRate null error")
    @Min(value = 0, message = "税率错误")
    private Integer taxRate;

    @NotNull(message = "executionStatus null error")
    @Pattern(regexp = "^(执行|中止)$", message = "executionStatus value error")
    private String executionStatus;

    @NotNull(message = "drawer null error")
    @NotBlank(message = "drawer blank error")
    private String drawer;

    @NotNull(message = "partnerCompanyID null error")
    @Min(value = 0, message = "公司错误")
    private Integer partnerCompanyID;
    // from c_partner_company
    private String companyAbbreviatedName;
    private String companyPhone;
    private String companyFullName;
    private String companyRemark;

    @NotNull(message = "departmentID null error")
    @Min(value = 0, message = "采购部门错误")
    private Integer departmentID;
    // from c_department
    private String departmentName;

    @NotNull(message = "warehouseID null error")
    @Min(value = 0, message = "采购仓库错误")
    private Integer warehouseID;
    // from w_warehouse
    private String warehouseName;

    @NotNull(message = "remark null error")
    private String remark;

    private int isModified;

    @Valid
    @NotEmpty(message = "采购单商品不能为空")
    private List<PurchaseOrderProductO> purchaseOrderProducts;
}
