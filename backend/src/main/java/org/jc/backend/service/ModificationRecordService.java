package org.jc.backend.service;

import org.jc.backend.entity.ModificationO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface ModificationRecordService {
    List<ModificationO> getRecordsBySerial(String serial);
}
