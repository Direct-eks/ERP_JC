package org.jc.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ModificationO {
    private int modificationRecordID;
    private String recordSerial;
    private String recordContent;
    private String modificationDate;
}
