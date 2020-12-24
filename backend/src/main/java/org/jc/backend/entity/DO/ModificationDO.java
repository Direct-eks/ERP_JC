package org.jc.backend.entity.DO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ModificationDO {
    private int modificationRecordID;
    private String recordSerial;
    private String recordContent;
    private String modificationDate;
}
