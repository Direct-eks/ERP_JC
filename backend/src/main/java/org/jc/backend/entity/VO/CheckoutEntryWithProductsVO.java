package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.InvoiceEntryO;
import org.jc.backend.entity.OutboundProductO;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@ToString
public class CheckoutEntryWithProductsVO {
    private String checkoutEntrySerial;

    @NotNull(message = "partnerCompanyID null error")
    private Integer partnerCompanyID;
    // from c_partner_company
    private String companyAbbreviatedName;
    private String companyFullName;
    private String companyPhone;

    @NotNull(message = "invoiceType null error")
    @Pattern(regexp = "^(增值税票|普票)$", message = "invoiceType value error")
    private String invoiceType;

    @NotNull(message = "paymentMethod null error")
    @Pattern(regexp = "^(现金|转支|电汇|汇票|其他)$", message = "paymentMethod value error")
    private String paymentMethod;

    @NotNull(message = "paymentNumber null error")
    private String paymentNumber;

    @NotNull(message = "paymentAmount null error")
    @NotBlank(message = "paymentAmount blank error")
    @Pattern(regexp = "^[\\d]*?\\.?[\\d]*?$", message = "paymentAmount value error")
    private String paymentAmount;

    @NotNull(message = "bankAccountID null error")
    private Integer bankAccountID;
    //from c_bank_account
    private String bankAccountName;

    @NotNull(message = "totalAmount null error")
    @NotBlank(message = "totalAmount blank error")
    @Pattern(regexp = "^[\\d]*?\\.?[\\d]*?$", message = "totalAmount value error")
    private String totalAmount;

    @NotNull(message = "debt null error")
    @NotBlank(message = "debt blank error")
    @Pattern(regexp = "^[\\d]*?\\.?[\\d]*?$", message = "debt value error")
    private String debt;

    @Min(value = 0, message = "isFollowUpIndication value error")
    @Max(value = 1, message = "isFollowUpIndication value error")
    private int isRounded;

    @NotNull(message = "roundedAmount null error") // pattern will allow blank
    @Pattern(regexp = "^[\\d]*?\\.?[\\d]*?$", message = "roundedAmount value error")
    private String roundedAmount;

    @NotNull(message = "serviceFee null error") // pattern will allow blank
    @Pattern(regexp = "^[\\d]*?\\.?[\\d]*?$", message = "serviceFee value error")
    private String serviceFee;

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

    private String moneyEntrySerial;
    private String returnSerial;

    @NotNull(message = "departmentID null error")
    private Integer departmentID;
    //from e_department
    private String departmentName;

    private int isVerified;
    private int isModified;

    private List<InboundProductO> inboundCheckoutProducts;
    private List<OutboundProductO> outboundCheckoutProducts;

    @Valid
    private InvoiceEntryO invoiceEntry;
}
