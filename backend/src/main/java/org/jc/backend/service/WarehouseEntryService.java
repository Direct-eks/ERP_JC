package org.jc.backend.service;

import org.springframework.stereotype.Indexed;

@Indexed
public interface WarehouseEntryService {
    void createProductionEntry();
}
