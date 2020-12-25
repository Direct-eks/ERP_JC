package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import org.jc.backend.entity.PurchaseOrderProductModifyO;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
public class PurchaseOrderModifyVO {
    private String purchaseOrderEntryID;
    @DecimalMin(value = "0.0", message = "totalCost smaller than zero error")
    private double totalCost;
    @Pattern(regexp = "^(增值税票|普票|收据)$", message = "invoiceType value error")
    private String invoiceType;
    @Pattern(regexp = "^(执行|中止)$", message = "executionStatus value error")
    private String executionStatus;
    private String drawer;

    private int departmentID;
    private int warehouseID;

    //only used for modification record
    private String departmentName;
    private String warehouseName;

    private String remark;

    // PurchaseOrderProductVO
    @Valid
    private List<PurchaseOrderProductModifyO> purchaseOrderProducts;
}
