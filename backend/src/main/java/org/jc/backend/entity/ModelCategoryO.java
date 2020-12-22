package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelCategoryO {
    private int modelCategoryID;
    private int sequenceNumber;
    private String code;
    private String name;
    private String remark;
    private String treeLevel;
}
