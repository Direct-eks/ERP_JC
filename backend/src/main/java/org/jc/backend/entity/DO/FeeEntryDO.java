package org.jc.backend.entity.DO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FeeEntryDO {
    private String feeEntryID;
    private String entryDate;
    private String creationDate;
    private String drawer;

    private int sourceAccountID;
    private String sourceAccountName;

    private int destinationAccountID;
    private String destinationAccountName;

    private String amount;
    private String number;

    private int departmentID;
    private String departmentName;

    private String remark;
    private int isBookKeeping;
    private int isVerified;
    private int isModified;

    private String inOrOut;
    private String balance;
}
