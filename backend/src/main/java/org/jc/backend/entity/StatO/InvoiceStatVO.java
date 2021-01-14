package org.jc.backend.entity.StatO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceStatVO {
    private int companyID;
    private String companyAbbreviatedName;
    private String companyFullName;
    private double totalAmount;
}
