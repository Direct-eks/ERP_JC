package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class InvoiceEntryO {
    private String invoiceEntrySerial;
    private int partnerCompanyID;

    @NotNull(message = "invoiceType null error")
    @Pattern(regexp = "^(增值税票|普票)$", message = "invoiceType value error")
    private String invoiceType;

    @NotNull(message = "invoiceNumber null error")
    private String invoiceNumber;

    @DecimalMin(value = "0.0", message = "totalAmount smaller than zero error")
    private double totalAmount;

    @DecimalMin(value = "0.0", message = "invoiceAmount smaller than zero error")
    private double invoiceAmount;

    @NotNull(message = "invoiceIndication null error")
    @Pattern(regexp = "^(正常|红冲)$", message = "invoiceIndication value error")
    private String invoiceIndication;

    private int isFollowUpIndication;

    @NotNull(message = "remark null error")
    private String remark;

    private String drawer;
    private String creationDate;
    private String checkoutDate;
    private String inOrOut;
    private String invoiceDate;
    private String invoiceNumberDate;
    private int isModified;
}
