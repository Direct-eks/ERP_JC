package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jc.backend.entity.OutboundProductO;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
@ToString
public class OutboundEntryWithProductsVO {
    private String outboundEntryID;

    @NotNull(message = "shipmentDate null error")
    @NotBlank(message = "shipmentDate blank error")
    private String shipmentDate;

    @NotNull(message = "creationDate null error")
    @NotBlank(message = "creationDate blank error")
    private String creationDate;

    @NotNull(message = "totalAmount null error")
    @NotBlank(message = "totalAmount blank error")
    @Pattern(regexp = "^[\\d]*?\\.?[\\d]*?$", message = "totalAmount value error")
    private String totalAmount;

    @NotNull(message = "deliveryMethod null error")
    @NotBlank(message = "deliveryMethod blank error")
    @Pattern(regexp = "^(自提|送货|代办发货|发货代收款)$", message = "deliveryMethod value error")
    private String deliveryMethod;

    @NotNull(message = "invoiceType null error")
    @NotBlank(message = "invoiceType blank error")
    @Pattern(regexp = "^(增值税票|普票|收据)$", message = "invoiceType value error")
    private String invoiceType;

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
    @Pattern(regexp = "^(销出|入退)$", message = "classification value error")
    private String classification;

    @NotNull(message = "shippingCost null error") // pattern will allow blank
    @Pattern(regexp = "^[\\d]*?\\.?[\\d]*?$", message = "shippingCost value error")
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
    private int shippingMethodID;
    // from c_relevant_company
    private String relevantCompanyName;

    private String shippingCostSerial;
    private String returnDate;
    private String returnSerial;
    private int printTimes;
    private int isModified;

    @Valid
    List<OutboundProductO> outboundProducts;
}
