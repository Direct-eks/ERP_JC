package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.EndUserO.EndUserDO;
import org.jc.backend.entity.EndUserO.UserPermission;
import org.jc.backend.entity.EndUserO.UserRole;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface EndUserMapper {

    EndUserDO queryUserByName(String username);

    String queryRoleByUserId(int id);
    List<String> queryPermissionsByUserId(int id);

    List<EndUserDO> queryAllUsers();
    List<UserRole> queryAllRoles();
    List<UserPermission> queryAllPermissions();

    int insertUserInfo(EndUserDO endUserDO);
    void updateUserInfo(EndUserDO endUserDO);
    void deleteUserInfo(int id);
    void insertUserRole(int id, int roleID);
    void updateUserRole(int id, int roleID);
    void deleteUserRole(int id);
    void insertUserPermission(int id, int permissionID);
    void deleteUserPermission(int id, int permissionID);
}
