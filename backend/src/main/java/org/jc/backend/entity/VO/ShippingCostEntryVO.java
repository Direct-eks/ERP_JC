package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import org.jc.backend.entity.DO.InboundEntryDO;
import org.jc.backend.entity.DO.OutboundEntryDO;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
public class ShippingCostEntryVO {
    private String shippingCostEntrySerial;

    private int partnerCompanyID;
    //from c_partner_company
    private String companyAbbreviatedName;
    private String companyFullName;
    private String companyPhone;

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

    private String creationDate;
    private String checkoutDate;

    private int inOrOut;
    private String invoiceDate;
    private int isModified;

    private List<InboundEntryDO> inboundEntries;
    private List<OutboundEntryDO> outboundEntries;
}
