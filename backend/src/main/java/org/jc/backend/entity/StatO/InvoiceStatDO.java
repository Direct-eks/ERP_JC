package org.jc.backend.entity.StatO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceStatDO {
    private int companyID;
    private String companyAbbreviatedName;
    private String companyFullName;
    private int quantity;
    private String unitPriceWithTax;
}
