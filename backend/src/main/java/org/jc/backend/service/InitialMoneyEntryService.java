package org.jc.backend.service;

import org.jc.backend.entity.InitialMoneyEntryO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface InitialMoneyEntryService {
    void createEntry(boolean isInbound, InitialMoneyEntryO newEntry);
    List<InitialMoneyEntryO> getEntries(boolean isInbound);
    InitialMoneyEntryO getEntryByCompanyID(boolean isInbound, int id);
    void modifyEntries(boolean isInbound, List<InitialMoneyEntryO> updateVO);
}
