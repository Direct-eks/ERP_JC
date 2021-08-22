package org.jc.backend.service;

import org.jc.backend.entity.ModificationO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface ModificationRecordService {
    void insertRecord(String serial, String content);
    void insertRecord(String serial, StringBuilder content);
    void insertRecord(String category, int id, String content);
    void insertRecord(String category, int id, StringBuilder content);
    List<ModificationO> getEntryRecordsBySerial(String serial);
    List<ModificationO> getMiscRecordsBySerial(String category, int id);
}
