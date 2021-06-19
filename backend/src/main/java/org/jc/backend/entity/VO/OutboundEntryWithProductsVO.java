package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jc.backend.config.validation.DecimalValidation;
import org.jc.backend.entity.OutboundProductO;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@ToString
public class OutboundEntryWithProductsVO {
    private String outboundEntryID;

    @NotNull(message = "shipmentDate null error")
    @NotBlank(message = "出库日期错误")
    private String shipmentDate;

    @NotNull(message = "creationDate null error")
    @NotBlank(message = "creationDate blank error")
    private String creationDate;

    @NotNull(message = "totalAmount null error")
    @DecimalValidation(type = DecimalValidation.ValidationTypeEnum.DECIMAL_8,
            message = "总金额校验错误")
    private String totalAmount;

    @NotNull(message = "deliveryMethod null error")
    @Pattern(regexp = "^(自提|送货|代办发货|发货代收款)$", message = "deliveryMethod value error")
    private String deliveryMethod;

    @NotNull(message = "invoiceType null error")
    @Pattern(regexp = "^(增值税票|普票|收据)$", message = "invoiceType value error")
    private String invoiceType;

    @NotNull(message = "taxRate null error")
    @Min(value = 0, message = "税率错误")
    private Integer taxRate;

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

    @NotNull(message = "departmentID null error")
    @Min(value = 0, message = "出库部门错误")
    private Integer departmentID;
    // from e_department
    private String departmentName;

    @NotNull(message = "warehouseID null error")
    @Min(value = 0, message = "出库仓库错误")
    private Integer warehouseID;
    // from w_warehouse
    private String warehouseName;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "classification null error")
    @Pattern(regexp = "^(销出|入退)$", message = "classification value error")
    private String classification;

    @NotNull(message = "shippingCost null error")
    @DecimalValidation(type = DecimalValidation.ValidationTypeEnum.DECIMAL_2,
            message = "运费值校验失败")
    private String shippingCost;

    @NotNull(message = "shippingCostType null error")
    @Pattern(regexp = "^(自付|代垫|无)$", message = "shippingCostType value error")
    private String shippingCostType;

    @NotNull(message = "shippingQuantity null error")
    @Min(value = 0, message = "运输件数不能小于0")
    private Integer shippingQuantity;

    @NotNull(message = "shippingNumber null error")
    private String shippingNumber;

    @NotNull(message = "shippingMethodID null error")
    @Min(value = -1, message = "shippingMethodID value error")
    private int shippingMethodID;
    // from c_relevant_company
    private String relevantCompanyName;

    private String shippingCostSerial;
    private String returnDate;
    private String returnSerial;
    private int printTimes;
    private int isModified;

    @Valid
    @NotEmpty(message = "出库单商品不能为空")
    List<OutboundProductO> outboundProducts;
}
