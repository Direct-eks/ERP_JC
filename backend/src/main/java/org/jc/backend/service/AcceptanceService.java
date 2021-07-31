package org.jc.backend.service;

import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.AcceptanceEntryO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface AcceptanceService {
    void createEntry(boolean isInbound, AcceptanceEntryO entryO) throws GlobalParamException;
    void createSolutionPayEntry();

    List<AcceptanceEntryO> getEntryByNumber(String number);
    List<AcceptanceEntryO> getEntriesInDateRange(String startDate, String endDate, String prefix);

    void updateEntry(boolean isInbound, AcceptanceEntryO entryO);
}
