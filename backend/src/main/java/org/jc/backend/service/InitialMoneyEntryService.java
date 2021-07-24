package org.jc.backend.service;

import org.jc.backend.entity.InitialMoneyEntryO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface InitialMoneyEntryService {
    void createEntry();
    List<InitialMoneyEntryO> getEntries(boolean isInbound);
    void modifyEntries(boolean isInbound, List<InitialMoneyEntryO> updateVO);
}
