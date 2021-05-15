package org.jc.backend.service;

import org.jc.backend.entity.EndUserO.*;

import java.util.List;

public interface EndUserService {

    EndUserDO getUserByName(String username);

    // for shiro usage, return string only
    String getRoleByUserId(int id);
    List<String> getPermissionsByUserId(int id);

    EndUserVO postUserLogInInfo(EndUserLoginVO user);

    void userLogout();

    List<String> queryUserNameList();

    List<EndUserVO> getAllUsers();
    List<String> getAllRoles();
    List<String> getAllPermissions();
}
