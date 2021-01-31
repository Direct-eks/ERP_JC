package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.OutboundProductO;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@ToString
public class InvoiceEntryStandAloneVO {
    private String invoiceEntrySerial;

    @NotNull(message = "partnerCompanyID null error")
    private Integer partnerCompanyID;
    // from c_partner_company
    private String companyFullName;

    @NotNull(message = "invoiceType null error")
    @Pattern(regexp = "^(增值税票|普票)$", message = "invoiceType value error")
    private String invoiceType;

    @NotNull(message = "invoiceNumber null error")
    @NotBlank(message = "invoiceNumber blank error")
    private String invoiceNumber;

    @NotNull(message = "totalAmount null error")
    @Pattern(regexp = "^(([1-9][\\d]*?\\.\\d+)|([1-9][\\d]*)|(0\\.[\\d]+)|(0))$",
            message = "totalAmount value error")
    private String totalAmount;

    @NotNull(message = "invoiceAmount null error")
    @Pattern(regexp = "^(([1-9][\\d]*?\\.\\d+)|([1-9][\\d]*)|(0\\.[\\d]+)|(0))$",
            message = "invoiceAmount value error")
    private String invoiceAmount;

    @NotNull(message = "invoiceIndication null error")
    @Pattern(regexp = "^(正常|红冲)$", message = "invoiceIndication value error")
    private String invoiceIndication;

    @Min(value = 0, message = "isFollowUpIndication value error")
    @Max(value = 1, message = "isFollowUpIndication value error")
    private int isFollowUpIndication;

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

    @NotNull(message = "invoiceNumberDate null error")
    @NotBlank(message = "invoiceNumberDate blank error")
    private String invoiceNumberDate;

    private int isModified;

    private List<InboundProductO> inboundInvoiceProducts;
    private List<OutboundProductO> outboundInvoiceProducts;
}
