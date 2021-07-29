package org.jc.backend.service;

import org.jc.backend.entity.AcceptanceEntryO;
import org.springframework.stereotype.Indexed;

@Indexed
public interface AcceptanceService {
    void createEntry(AcceptanceEntryO entryO);
}
