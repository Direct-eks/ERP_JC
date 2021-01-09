package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.DO.ShippingCostEntryDO;
import org.jc.backend.entity.MoneyEntryO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ShippingCostEntryMapper {
    int countNumberOfEntriesOfToday(String prefix);
    void insertEntry(ShippingCostEntryDO shippingCostEntryDO);
    List<ShippingCostEntryDO> getEntriesInDateRangeAndParams(String startDate, String endDate,
                                                     int companyID, String prefix);
    MoneyEntryO selectEntryBySerial(String serial);
    void modifyEntry(ShippingCostEntryDO shippingCostEntryDO);
}
