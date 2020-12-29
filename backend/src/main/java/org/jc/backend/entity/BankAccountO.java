package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountO {
    private int bankAccountID;
    private String name;
    private String accountNumber;
    private String remark;
    private int isVisible;
    private int inUse;
}
