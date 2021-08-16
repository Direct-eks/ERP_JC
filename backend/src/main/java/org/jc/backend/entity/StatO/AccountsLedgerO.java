package org.jc.backend.entity.StatO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountsLedgerO {
    private int companyID;
    private String companyAbbreviatedName;
    private String companyFullName;
    private String debitOrCredit;
    private String checkoutAmount;
    private String invoicedAmount;
}
