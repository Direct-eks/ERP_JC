package org.jc.backend.entity.DO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WarehouseEntryDO {
    private String warehouseEntryID;
    private String entryDate;
    private String creationDate;
    private String totalAmount;
    private String drawer;

    private int departmentID;
    // from e_department
    private String departmentName;

    private int warehouseID;
    // from w_warehouse
    private String warehouseName;

    private String remark;
    private String relatedEntrySerial;

    private String classification;
}
