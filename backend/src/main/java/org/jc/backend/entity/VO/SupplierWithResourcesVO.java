package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jc.backend.entity.SupplierResourceO;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class SupplierWithResourcesVO {

    @NotNull(message = "supplierID null error")
    private Integer supplierID;
    // from c_partner_company
    private String supplierAbbreviatedName;
    private String supplierFullName;
    private String supplierPhone;

    @NotNull(message = "lastQuoteDate null error")
    @NotBlank(message = "lastQuoteDate blank error")
    private String lastQuoteDate;

    @NotNull(message = "thisQuoteDate null error")
    @NotBlank(message = "thisQuoteDate blank error")
    private String thisQuoteDate;

    @NotNull(message = "remark null error")
    private String remark;

    @NotEmpty
    @Valid
    private List<SupplierResourceO> resources;
}
