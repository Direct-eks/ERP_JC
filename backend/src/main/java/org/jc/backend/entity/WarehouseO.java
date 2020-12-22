package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseO {
    private int warehouseID;
    private String name;
    private String location;
    private int isDefault;
}
