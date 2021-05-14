package org.jc.backend.service;

import org.jc.backend.entity.EndUserO.*;

import java.util.List;

public interface EndUserService {

    EndUserDO getUserByName(String username);

    String getRoleByUserId(int id);

    List<String> getPermissionsByUserId(int id);

    EndUserVO postUserLogInInfo(EndUserLoginVO user);

    void userLogout();

    List<String> queryUserNameList();

    List<UserRole> getAllRoles();
    List<UserPermission> getAllPermissions();
}
