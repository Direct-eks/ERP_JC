package org.jc.backend.service;

import org.jc.backend.entity.DTO.EndUserDTO;

import java.util.List;

public interface EndUserService {

    EndUserDTO getUserByName(String username);

    String getRoleByUserId(int id);

    List<String> getPermissionsByUserId(int id);

    String postUserLogInInfo(EndUserDTO user);

    void userLogout();


    List<EndUserDTO> queryUsers();
}
