package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FeeCategoryO {
    private int feeCategoryID;
    private String name;
    private String remark;
    private String classification;
    private String treeLevel;
}
