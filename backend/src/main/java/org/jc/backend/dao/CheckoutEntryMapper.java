package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.DO.CheckoutEntryDO;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CheckoutEntryMapper {
    int countNumberOfEntriesOfToday(String prefix);
    void insertEntry(CheckoutEntryDO checkoutEntryDO);
}
