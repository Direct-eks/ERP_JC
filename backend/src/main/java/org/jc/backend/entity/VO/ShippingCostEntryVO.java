package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jc.backend.config.validation.DecimalValidation;
import org.jc.backend.entity.DO.InboundEntryDO;
import org.jc.backend.entity.DO.OutboundEntryDO;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@ToString
public class ShippingCostEntryVO {
    private String shippingCostEntrySerial;

    @NotNull(message = "partnerCompanyID null error")
    private Integer partnerCompanyID;
    //from c_partner_company
    private String companyAbbreviatedName;
    private String companyFullName;
    private String companyPhone;

    @Min(value = 0, message = "isTaxDeduction value error")
    @Max(value = 1, message = "isTaxDeduction value error")
    private int isTaxDeduction;

    @NotNull(message = "invoiceNumber null error")
    private String invoiceNumber;

    @NotNull(message = "totalAmount null error")
    @DecimalValidation(type = DecimalValidation.ValidationTypeEnum.DECIMAL_2,
            message = "总金额错误")
    private String totalAmount;

    @NotNull(message = "invoiceAmount null error")
    @DecimalValidation(type = DecimalValidation.ValidationTypeEnum.DECIMAL_2,
            message = "开票金额错误")
    private String invoiceAmount;

    @NotNull(message = "shippingPaymentType null error")
    @Pattern(regexp = "^(代垫|自付)$", message = "shippingCostType value error")
    private String shippingCostType;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "drawer null error")
    @NotBlank(message = "drawer blank error")
    private String drawer;

    @NotNull(message = "creationDate null error")
    @NotBlank(message = "creationDate blank error")
    private String creationDate;

    @NotNull(message = "checkoutDate null error")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "入库结账日期错误")
    private String checkoutDate;

    @Min(value = 0, message = "inOrOut value error")
    @Max(value = 1, message = "inOrOut value error")
    private int inOrOut;

    @NotNull(message = "invoiceDate null error")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "入库开票日期错误")
    private String invoiceDate;

    private int isModified;

    private List<InboundEntryDO> inboundEntries;
    private List<OutboundEntryDO> outboundEntries;
}
