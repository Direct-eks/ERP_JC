package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import org.jc.backend.entity.PurchaseOrderProductO;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
public class PurchaseOrderEntryWithProductsVO {
    private String purchaseOrderEntryID;
    @NotNull(message = "entryDate null error")
    @NotBlank(message = "entryDate blank error")
    private String entryDate;
    @NotNull(message = "creationDate null error")
    @NotBlank(message = "creationDate blank error")
    private String creationDate;
    @DecimalMin(value = "0.0", message = "totalCost smaller than zero error")
    private double totalCost;
    @Pattern(regexp = "^(增值税票|普票|收据)$", message = "invoiceType value error")
    private String invoiceType;
    @Pattern(regexp = "^(执行|中止)$", message = "executionStatus value error")
    private String executionStatus;
    @NotNull(message = "drawer null error")
    @NotBlank(message = "drawer blank error")
    private String drawer;

    private int partnerCompanyID;
    // from c_partner_company
    private String companyAbbreviatedName;
    private String companyPhone;
    private String companyFullName;

    private int departmentID;
    // from c_department
    private String departmentName;

    private int warehouseID;
    // from w_warehouse
    private String warehouseName;

    private String remark;

    // PurchaseOrderProductVO
    @Valid
    private List<PurchaseOrderProductO> purchaseOrderProducts;
}
