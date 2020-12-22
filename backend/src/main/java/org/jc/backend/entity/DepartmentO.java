package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentO {
    private int departmentID;
    private String name;
    private String remark;
    private int isDefault;
}
