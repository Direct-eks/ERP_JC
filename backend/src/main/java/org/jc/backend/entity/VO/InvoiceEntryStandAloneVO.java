package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.OutboundProductO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
public class InvoiceEntryStandAloneVO {
    private String invoiceEntrySerial;

    private int partnerCompanyID;
    // from c_partner_company
    private String companyFullName;

    @NotNull(message = "invoiceType null error")
    @Pattern(regexp = "^(增值税票|普票)$", message = "invoiceType value error")
    private String invoiceType;

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

    @NotNull(message = "invoiceIndication null error")
    @Pattern(regexp = "^(正常|红冲)$", message = "invoiceIndication value error")
    private String invoiceIndication;

    private int isFollowUpIndication;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "drawer null error")
    @NotBlank(message = "drawer blank error")
    private String drawer;

    private String creationDate;
    private String checkoutDate;
    private String inOrOut;
    private String invoiceDate;
    private String invoiceNumberDate;
    private int isModified;

    private List<InboundProductO> inboundInvoiceProducts;
    private List<OutboundProductO> outboundInvoiceProducts;
}
