package org.jc.backend.service;

import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface MiscellaneousDataService {
    List<String> getAllTaxRateOptions();
    String getLastWarehouseStockUpdateTime();
    void updateLastBackupTime();
}
