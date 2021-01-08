package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import org.jc.backend.entity.SalesOrderProductO;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
public class SalesOrderEntryWithProductsVO {
    private String salesOrderEntryID;

    @NotNull(message = "shipmentDate null error")
    @NotBlank(message = "shipmentDate blank error")
    private String shipmentDate;
    private String creationDate;

    @DecimalMin(value = "0.0", message = "totalCost smaller than zero error")
    private double totalAmount;

    @Pattern(regexp = "^(增值税票|普票|收据)$", message = "invoiceType value error")
    private String invoiceType;

    @Pattern(regexp = "^(执行|中止)$", message = "executionStatus value error")
    private String executionStatus;

    @NotNull(message = "drawer null error")
    @NotBlank(message = "drawer blank error")
    private String drawer;

    private int partnerCompanyID;
    //from c_partner_company
    private String companyAbbreviatedName;
    private String companyFullName;
    private String companyPhone;

    private int departmentID;
    //from e_department
    private String departmentName;

    private int warehouseID;
    //from w_warehouse
    private String warehouseName;

    private String remark;
    private int isModified;

    @Valid
    private List<SalesOrderProductO> salesOrderProducts;
}
