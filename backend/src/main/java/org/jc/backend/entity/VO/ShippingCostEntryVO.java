package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
    @NotBlank(message = "totalAmount blank error")
    @Pattern(regexp = "^[\\d]*?\\.?[\\d]*?$", message = "totalAmount value error")
    private String totalAmount;

    @NotNull(message = "invoiceAmount null error")
    @NotBlank(message = "invoiceAmount blank error")
    @Pattern(regexp = "^[\\d]*?\\.?[\\d]*?$", message = "invoiceAmount value error")
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
    @NotBlank(message = "checkoutDate blank error")
    private String checkoutDate;

    @Min(value = 0, message = "inOrOut value error")
    @Max(value = 1, message = "inOrOut value error")
    private int inOrOut;

    @NotNull(message = "invoiceDate null error")
    @NotBlank(message = "invoiceDate blank error")
    private String invoiceDate;

    private int isModified;

    private List<InboundEntryDO> inboundEntries;
    private List<OutboundEntryDO> outboundEntries;
}
