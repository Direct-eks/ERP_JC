package org.jc.backend.entity.DO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierDO {
    private int supplierID;
    // from c_partner_company
    private String supplierAbbreviatedName;
    private String supplierFullName;
    private String supplierPhone;

    private String lastQuoteDate;
    private String thisQuoteDate;
    private String remark;
}
