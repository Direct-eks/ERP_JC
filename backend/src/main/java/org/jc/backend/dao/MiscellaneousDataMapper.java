package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.MiscellaneousDataO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface MiscellaneousDataMapper {
    List<MiscellaneousDataO> queryAllTaxRateOptions();

    String queryLastBackupTime();
    void updateLastBackupTime();

    String queryPermittedRoundingAmountByUser(String username);
    void insertPermittedRoundingAmountByUser(String username, String amount);
    void updatePermittedRoundingAmountByUser(String username, String amount);
    void deletePermittedRoundingAmountByUser(String username);

    List<MiscellaneousDataO> queryAuditMonths();
    void insertAuditMonth(String month, String value);
    void deleteAuditMonth(String month, String value);
}
