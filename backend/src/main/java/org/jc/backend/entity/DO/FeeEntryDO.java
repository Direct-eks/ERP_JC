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
    private int source_account_id;
    private int destination_account_id;
    private String amount;
    private String number;
    private String remark;
    private int isBookKeeping;
    private int isVerified;
}
