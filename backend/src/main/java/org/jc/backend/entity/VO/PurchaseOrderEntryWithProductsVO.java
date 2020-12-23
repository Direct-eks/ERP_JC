package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PurchaseOrderEntryWithProductsVO {
    private String purchaseOrderEntryID;
    private String serial;
    @NotNull(message = "entryDate null error")
    private Date entryDate;
    @NotNull(message = "creationDate null error")
    private Date creationDate;
    private double totalCost;
    private String invoiceType;
    private String executionStatus;
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
    private List<PurchaseOrderProductVO> purchaseOrderProducts;
}
