package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import org.jc.backend.entity.InboundProductO;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
public class InboundEntryWithProductsVO {
    private String inboundEntryID;

    @NotNull(message = "entryDate null error")
    @NotBlank(message = "entryDate blank error")
    private String entryDate;

    private String creationDate;

    @NotNull(message = "totalCost null error")
    @NotBlank(message = "totalCost blank error")
    @Pattern(regexp = "^[\\d]*?\\.?[\\d]*?$", message = "totalCost value error")
    private String totalCost;

    @NotNull(message = "invoiceType null error")
    @Pattern(regexp = "^(增值税票|普票|收据)$", message = "invoiceType value error")
    private String invoiceType;

    @NotNull(message = "drawer null error")
    @NotBlank(message = "drawer blank error")
    private String drawer;

    private int partnerCompanyID;
    // from c_partner_company
    private String companyAbbreviatedName;
    private String companyPhone;
    private String companyFullName;

    private int departmentID;
    // from e_department
    private String departmentName;

    private int warehouseID;
    // from w_warehouse
    private String warehouseName;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "classification null error")
    @NotBlank(message = "classification blank error")
    @Pattern(regexp = "^(购入|出退)$", message = "classification value error")
    private String classification;

    @NotNull(message = "shippingCost null error") // pattern will allow blank
    @Pattern(regexp = "^[\\d]*?\\.?[\\d]*?$", message = "shippingCost value error")
    private String shippingCost;

    @NotNull(message = "shippingCostType null error")
    @Pattern(regexp = "^(自付|代垫|无)$", message = "shippingCostType value error")
    private String shippingCostType;

    @Min(value = 0, message = "shippingQuantity smaller than zero error")
    private int shippingQuantity;

    @NotNull(message = "shippingNumber null error")
    private String shippingNumber;

    private int shippingMethodID;
    // from c_relevant_company
    private String relevantCompanyName;

    private String shippingCostSerial;
    private String returnDate;
    private String returnSerial;
    private int printTimes;
    private int isModified;

    @Valid
    List<InboundProductO> inboundProducts;
}
