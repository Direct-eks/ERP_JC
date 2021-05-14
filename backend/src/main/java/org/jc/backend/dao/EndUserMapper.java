package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.EndUserO.EndUserDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EndUserMapper {

    EndUserDO queryUserByName(String username);

    String queryRoleByUserId(int id);

    List<String> queryPermissionsByUserId(int id);

    List<EndUserDO> queryAllUsers();
}
