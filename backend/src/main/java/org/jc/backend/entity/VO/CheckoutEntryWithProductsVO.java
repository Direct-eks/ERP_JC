package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jc.backend.config.validation.DecimalValidation;
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
    private String companyRemark;

    @NotNull(message = "invoiceType null error")
    @Pattern(regexp = "^(增值税票|普票)$", message = "invoiceType value error")
    private String invoiceType;

    @NotNull(message = "paymentMethod null error")
    @Pattern(regexp = "^(现金|转支|电汇|汇票|其他)$", message = "paymentMethod value error")
    private String paymentMethod;

    @NotNull(message = "paymentNumber null error")
    private String paymentNumber;

    @NotNull(message = "paymentAmount null error")
    @DecimalValidation(type = DecimalValidation.ValidationTypeEnum.DECIMAL_2,
            message = "付款错误")
    private String paymentAmount;

    @NotNull(message = "bankAccountID null error")
    private Integer bankAccountID;
    //from c_bank_account
    private String bankAccountName;

    @NotNull(message = "totalAmount null error")
    @DecimalValidation(type = DecimalValidation.ValidationTypeEnum.DECIMAL_2,
            message = "总金额错误")
    private String totalAmount;

    @NotNull(message = "debt null error")
    @DecimalValidation(type = DecimalValidation.ValidationTypeEnum.DECIMAL_2,
            message = "余额错误")
    private String debt;

    @Min(value = 0, message = "isFollowUpIndication value error")
    @Max(value = 1, message = "isFollowUpIndication value error")
    private int isRounded;

    @NotNull(message = "roundedAmount null error")
    @DecimalValidation(type = DecimalValidation.ValidationTypeEnum.DECIMAL_2,
            message = "抹零值错误")
    private String roundedAmount;

    @NotNull(message = "serviceFee null error")
    @DecimalValidation(type = DecimalValidation.ValidationTypeEnum.DECIMAL_2,
            message = "服务费错误")
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
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "入库结账日期错误")
    private String checkoutDate;

    private String moneyEntrySerial;
    private String invoiceEntrySerial;
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
