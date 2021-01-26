package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jc.backend.entity.QuotaProductO;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
@ToString
public class QuotaEntryWithProductsVO {
    private String quotaEntryID;

    @NotNull(message = "creationDate null error")
    @NotBlank(message = "creationDate blank error")
    private String creationDate;

    @NotNull(message = "totalAmount null error")
    @NotBlank(message = "totalAmount blank error")
    @Pattern(regexp = "^[\\d]*?\\.?[\\d]*?$", message = "totalAmount value error")
    private String totalAmount;

    @NotNull(message = "invoiceType null error")
    @Pattern(regexp = "^(增值税票|普票|收据)$", message = "invoiceType value error")
    private String invoiceType;

    @NotNull(message = "drawer null error")
    @NotBlank(message = "drawer blank error")
    private String drawer;

    @NotNull(message = "partnerCompanyID null error")
    private Integer partnerCompanyID;
    // from c_partner_company
    private String companyAbbreviatedName;
    private String companyFullName;
    private String companyPhone;

    @NotNull(message = "remark null error")
    private String remark;

    private int isModified;

    @Valid
    private List<QuotaProductO> quotaProducts;
}
