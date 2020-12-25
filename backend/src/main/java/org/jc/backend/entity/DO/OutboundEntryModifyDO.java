package org.jc.backend.entity.DO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OutboundEntryModifyDO {
    private String outboundEntryID;
    private double totalAmount;
    private String deliverMethod;
    private String invoiceType;
    private String drawer;

    private int departmentID;
    // from e_department
    private String departmentName;

    private String remark;
}
