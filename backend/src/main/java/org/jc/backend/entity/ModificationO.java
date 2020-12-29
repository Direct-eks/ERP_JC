package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModificationO {
    private int modificationRecordID;
    private String recordSerial;
    private String recordContent;
    private String modificationDate;

    public ModificationO(String recordSerial, String recordContent) {
        this.recordSerial = recordSerial;
        this.recordContent = recordContent;
    }
}
