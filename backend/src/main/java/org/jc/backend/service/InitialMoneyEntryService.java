package org.jc.backend.service;

import org.jc.backend.entity.InitialMoneyEntryO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface InitialMoneyEntryService {
    List<InitialMoneyEntryO> getEntries(boolean isInbound);
    InitialMoneyEntryO getEntryByCompanyID(boolean isInbound, int id);
    void updateEntries(boolean isInbound, List<InitialMoneyEntryO> updateVO);
}
