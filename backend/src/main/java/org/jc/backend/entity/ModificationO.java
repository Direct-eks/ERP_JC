package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ModificationO {
    private int modificationRecordID;

    // for entry modification
    private String recordSerial;
    // for misc modification
    private String recordCategory;
    private int recordID;

    private String recordContent;
    private String modificationTime;

    public ModificationO(String recordSerial, String recordContent) {
        this.recordSerial = recordSerial;
        this.recordContent = recordContent;
    }

    public ModificationO(String recordCategory, int recordID, String recordContent) {
        this.recordCategory = recordCategory;
        this.recordID = recordID;
        this.recordContent = recordContent;
    }
}
