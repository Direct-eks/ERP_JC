package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.InvoiceEntryO;
import org.jc.backend.entity.OutboundProductO;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
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

    @DecimalMin(value = "0.0", message = "paymentAmount smaller than zero error")
    private double paymentAmount;

    private int bankAccountID;
    //from c_bank_account
    private String bankAccountName;

    @DecimalMin(value = "0.0", message = "totalAmount smaller than zero error")
    private double totalAmount;
    private double debt;

    private int isRounded;
    private double roundedAmount;

    @DecimalMin(value = "0.0", message = "serviceFee smaller than zero error")
    private double serviceFee;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "drawer null error")
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
