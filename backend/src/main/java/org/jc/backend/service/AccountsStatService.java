package org.jc.backend.service;

import org.jc.backend.entity.StatO.MoneyEntryDetailO;

import java.util.List;

public interface AccountsStatService {
    List<MoneyEntryDetailO> getEntryDetails(boolean isInbound);

}
