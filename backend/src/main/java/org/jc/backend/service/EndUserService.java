package org.jc.backend.service;

import org.jc.backend.entity.EndUserO.EndUserDO;
import org.jc.backend.entity.EndUserO.EndUserLoginVO;
import org.jc.backend.entity.EndUserO.EndUserVO;

import java.util.List;

public interface EndUserService {

    EndUserDO getUserByName(String username);

    String getRoleByUserId(int id);

    List<String> getPermissionsByUserId(int id);

    EndUserVO postUserLogInInfo(EndUserLoginVO user);

    void userLogout();

    List<String> queryUserNameList();
}
