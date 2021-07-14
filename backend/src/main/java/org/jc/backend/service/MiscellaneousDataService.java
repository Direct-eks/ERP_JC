package org.jc.backend.service;

import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface MiscellaneousDataService {
    List<String> getAllTaxRateOptions();

    String getLastBackupTime();
    void updateLastBackupTime();

    String getPermittedRoundingAmountByUser(String username);
    void insertPermittedRoundingAmountByUser(String username, String amount);
    void updatePermittedRoundingAmountByUser(String username, String amount);
    void deletePermittedRoundingAmountByUser(String username);

    List<String> queryAuditMonths();
    void addNewAuditMonth(String month);
    void deleteAuditMonth(String month);
}
