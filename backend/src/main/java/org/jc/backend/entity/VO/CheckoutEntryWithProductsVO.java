package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.InvoiceEntryO;
import org.jc.backend.entity.OutboundProductO;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
public class CheckoutEntryWithProductsVO {
    private String checkoutEntrySerial;
    private int partnerCompanyID;
    private String companyAbbreviatedName;
    private String companyFullName;
    private String companyPhone;

    @NotNull(message = "invoiceType null error")
    @Pattern(regexp = "^(增值税票|普票)$", message = "invoiceType value error")
    private String invoiceType;

    private String paymentMethod;
    private String paymentNumber;

    @NotNull(message = "paymentAmount null error")
    @NotBlank(message = "paymentAmount blank error")
    @Pattern(regexp = "^[\\d]*?\\.?[\\d]*?$", message = "paymentAmount value error")
    private String paymentAmount;

    private int bankAccountID;
    //from c_bank_account
    private String bankAccountName;

    @NotNull(message = "totalAmount null error")
    @NotBlank(message = "totalAmount blank error")
    @Pattern(regexp = "^[\\d]*?\\.?[\\d]*?$", message = "totalAmount value error")
    private String totalAmount;

    @NotNull(message = "debt null error")
    private String debt;

    private int isRounded;

    @NotNull(message = "roundedAmount null error")
    private String roundedAmount;

    @NotNull(message = "totalAmount null error")
//    @NotBlank(message = "totalAmount blank error")
//    @Pattern(regexp = "^[\\d]*?\\.?[\\d]*?$", message = "totalAmount value error")
    private String serviceFee;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "drawer null error")
    @NotBlank(message = "drawer blank error")
    private String drawer;

    private String creationDate;
    private String checkoutDate;
    private String moneyEntrySerial;
    private String returnSerial;

    private int departmentID;
    //from e_department
    private String departmentName;

    private int isVerified;
    private int isModified;

    private List<InboundProductO> inboundCheckoutProducts;
    private List<OutboundProductO> outboundCheckoutProducts;

    @Valid
    private InvoiceEntryO invoiceEntry;
}
