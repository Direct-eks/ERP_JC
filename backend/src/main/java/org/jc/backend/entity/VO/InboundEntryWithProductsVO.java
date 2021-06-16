package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jc.backend.entity.InboundProductO;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
@ToString
public class InboundEntryWithProductsVO {
    private String inboundEntryID;

    @NotNull(message = "entryDate null error")
    @NotBlank(message = "entryDate blank error")
    private String entryDate;

    @NotNull(message = "creationDate null error")
    @NotBlank(message = "creationDate blank error")
    private String creationDate;

    @NotNull(message = "totalCost null error")
    @Pattern(regexp = "^(([1-9][\\d]*?\\.\\d+)|([1-9][\\d]*)|(0\\.[\\d]+)|(0))$",
            message = "totalCost value error")
    private String totalCost;

    @NotNull(message = "invoiceType null error")
    @Pattern(regexp = "^(增值税票|普票|收据)$", message = "invoiceType value error")
    private String invoiceType;

    @NotNull(message = "taxRate null error")
    private Integer taxRate;

    @NotNull(message = "drawer null error")
    @NotBlank(message = "drawer blank error")
    private String drawer;

    @NotNull(message = "partnerCompanyID null error")
    private Integer partnerCompanyID;
    // from c_partner_company
    private String companyAbbreviatedName;
    private String companyPhone;
    private String companyFullName;

    @NotNull(message = "departmentID null error")
    private Integer departmentID;
    // from e_department
    private String departmentName;

    @NotNull(message = "warehouseID null error")
    private Integer warehouseID;
    // from w_warehouse
    private String warehouseName;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "classification null error")
    @NotBlank(message = "classification blank error")
    @Pattern(regexp = "^(购入|出退)$", message = "classification value error")
    private String classification;

    @NotNull(message = "shippingCost null error")
    @Pattern(regexp = "^(([1-9][\\d]*?\\.\\d+)|([1-9][\\d]*)|(0\\.[\\d]+)|(0))$",
            message = "shippingCost value error")
    private String shippingCost;

    @NotNull(message = "shippingCostType null error")
    @Pattern(regexp = "^(自付|代垫|无)$", message = "shippingCostType value error")
    private String shippingCostType;

    @NotNull(message = "shippingQuantity null error")
    @Min(value = 0, message = "shippingQuantity smaller than zero error")
    private Integer shippingQuantity;

    @NotNull(message = "shippingNumber null error")
    private String shippingNumber;

    @NotNull(message = "shippingMethodID null error")
    private Integer shippingMethodID;
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
