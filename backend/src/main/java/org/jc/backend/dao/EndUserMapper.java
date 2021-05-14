package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.EndUserO.EndUserDO;
import org.jc.backend.entity.EndUserO.UserPermission;
import org.jc.backend.entity.EndUserO.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EndUserMapper {

    EndUserDO queryUserByName(String username);

    UserRole queryRoleByUserId(int id);

    List<UserPermission> queryPermissionsByUserId(int id);

    List<EndUserDO> queryAllUsers();

    List<UserRole> queryAllRoles();
    List<UserPermission> queryAllPermissions();
}
