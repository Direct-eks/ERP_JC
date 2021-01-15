package org.jc.backend.service;

import org.jc.backend.entity.DO.EndUserDO;
import org.jc.backend.entity.VO.EndUserLoginVO;
import org.jc.backend.entity.VO.EndUserVO;

import java.util.List;

public interface EndUserService {

    EndUserDO getUserByName(String username);

    String getRoleByUserId(int id);

    List<String> getPermissionsByUserId(int id);

    EndUserVO postUserLogInInfo(EndUserLoginVO user);

    void userLogout();


    List<EndUserVO> queryUsers();
}
