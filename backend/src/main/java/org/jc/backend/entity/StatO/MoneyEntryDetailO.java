package org.jc.backend.entity.StatO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MoneyEntryDetailO {
    private String entryID;
    private String entryDate;
    private String explanation;
    private String debtorAmount;
    private String creditorAmount;
    private String auditAmount;
    private String debitOrCredit;
    private String balance;
    // for temp value storage
    private String amount;
}
