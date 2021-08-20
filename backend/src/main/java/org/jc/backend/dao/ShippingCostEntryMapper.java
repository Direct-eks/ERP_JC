package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.DO.ShippingCostEntryDO;
import org.jc.backend.entity.StatO.AccountsDetailO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface ShippingCostEntryMapper {
    int countNumberOfEntriesOfToday(String prefix);
    void insertEntry(ShippingCostEntryDO shippingCostEntryDO);
    List<ShippingCostEntryDO> getEntriesInDateRangeAndParams(String startDate, String endDate,
                                                     int companyID, String prefix);
    ShippingCostEntryDO selectEntryBySerialForCompare(String serial);
    void modifyEntry(ShippingCostEntryDO shippingCostEntryDO);

    // for accounts service
    List<Integer> queryDistinctCompanyIDs();
    List<ShippingCostEntryDO> queryAllEntriesByPrefixAndCompany(String prefix, int id);
    void updateEntryBalanceBySerial(AccountsDetailO entry);
}
