package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import org.jc.backend.entity.DO.InboundEntryDO;
import org.jc.backend.entity.DO.OutboundEntryDO;

import javax.validation.constraints.DecimalMin;
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

    @DecimalMin(value = "0.0", message = "totalAmount smaller than zero error")
    private double totalAmount;

    @DecimalMin(value = "0.0", message = "invoiceAmount smaller than zero error")
    private double invoiceAmount;

    @NotNull(message = "shippingPaymentType null error")
    @Pattern(regexp = "^(代垫|自付)$", message = "shippingPaymentType value error")
    private String shippingPaymentType;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "drawer null error")
    private String drawer;

    private String creationDate;
    private String checkoutDate;

    private int inOrOut;
    private String invoiceDate;
    private int isModified;

    private List<InboundEntryDO> inboundEntries;
    private List<OutboundEntryDO> outboundEntries;
}
