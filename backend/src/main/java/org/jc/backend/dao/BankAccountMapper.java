package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.BankAccountO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface BankAccountMapper {
    void insertAccount(BankAccountO bankAccountO);
    void updateAccount(BankAccountO bankAccountO);
    void deleteAccount(int id);
    List<BankAccountO> queryAllAccounts();
    List<BankAccountO> queryVisibleAccounts();
}
