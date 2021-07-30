package org.jc.backend.service;

import org.jc.backend.entity.AcceptanceEntryO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface AcceptanceService {
    void createEntry(boolean isInbound, AcceptanceEntryO entryO);
    void createSolutionPayEntry();

    AcceptanceEntryO getEntryByNumber(String number);
    List<AcceptanceEntryO> getEntriesInDateRange(String startDate, String endDate, String classification);

}
