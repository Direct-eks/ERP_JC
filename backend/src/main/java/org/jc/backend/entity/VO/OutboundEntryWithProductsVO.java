package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import org.jc.backend.entity.OutboundProductO;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
public class OutboundEntryWithProductsVO {
    private String outboundEntryID;

    @NotNull(message = "shipmentDate null error")
    @NotBlank(message = "shipmentDate blank error")
    private String shipmentDate;

    private String creationDate;

    @DecimalMin(value = "0.0", message = "totalAmount smaller than zero error")
    private double totalAmount;

    @Pattern(regexp = "^(自提|送货|代办发货|发货代收款)$", message = "deliveryMethod value error")
    private String deliveryMethod;

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

    private String remark;

    @Pattern(regexp = "^(销出|入退)$", message = "classification value error")
    private String classification;

    @DecimalMin(value = "0.0", message = "shipping cost smaller than zero error")
    private double shippingCost;
    private String shippingCostType;
    private int shippingQuantity;
    private String shippingNumber;

    private int shippingMethodID;
    // from c_relevant_company
    private String relevantCompanyName;

    private String shippingCostSerial;
    private String returnDate;
    private String returnSerial;
    private int printTimes;
    private int isModified;

    List<OutboundProductO> outboundProducts;
}
