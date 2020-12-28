package org.jc.backend.service;

import org.jc.backend.entity.ModificationO;

import java.util.List;

public interface ModificationRecordService {
    List<ModificationO> getRecordsBySerial(String serial);
}
