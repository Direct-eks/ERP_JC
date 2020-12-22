package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelO {
    private int modelID;
    private int sequenceNumber;
    private String newCode;
    private String oldCode;
    private int categoryID;
    private int unitID;
}
