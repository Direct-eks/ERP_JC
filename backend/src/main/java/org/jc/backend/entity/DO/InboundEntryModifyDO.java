package org.jc.backend.entity.DO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InboundEntryModifyDO {
    private String inboundEntryID;
    private double totalCost;
    private String invoiceType;
    private String drawer;

    private int departmentID;
    // from e_department
    private String departmentName;

    private String remark;
    private int isModified;
}
