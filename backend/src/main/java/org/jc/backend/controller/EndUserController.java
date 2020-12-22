package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.jc.backend.entity.DTO.EndUserDTO;
import org.jc.backend.entity.VO.EndUserLoginVO;
import org.jc.backend.entity.VO.EndUserVO;
import org.jc.backend.service.EndUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "User Related")
@RestController
@RequestMapping("/user")
public class EndUserController {

    private static final Logger logger = LoggerFactory.getLogger(EndUserController.class);

    private final EndUserService endUserService;

    public EndUserController(EndUserService endUserService) {
        this.endUserService = endUserService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "user login api", response = String.class)
    @PostMapping("/userAuthentication")
    public String authenticate(@RequestBody @Validated EndUserLoginVO endUserLoginVO) {
        logger.info("POST Request to /userAuthentication");
        EndUserDTO userDTO = new EndUserDTO();
        BeanUtils.copyProperties(endUserLoginVO, userDTO);
        return endUserService.postUserLogInInfo(userDTO);
    }

    @ApiOperation(value = "user logout api")
    @PostMapping("/userLogout")
    public void logout() {
        logger.info("POST Request to /userLogout");
        endUserService.userLogout();
    }

    @ApiOperation(value = "", response = Boolean.class)
    @PostMapping("/changePassword")
    public Boolean changePassword(@RequestBody @Validated EndUserLoginVO endUserLoginVO) {
        logger.info("POST Request to /changePassword");
        EndUserDTO userDTO = new EndUserDTO();
        BeanUtils.copyProperties(endUserLoginVO, userDTO);
        //todo
        return false;
    }

    @ApiOperation(value = "", response = Boolean.class)
    @PostMapping("/createNewUser")
    public Boolean createNewUser(@RequestBody @Validated EndUserVO endUserVO) {
        logger.info("POST Request to /createNewUser");
        //todo
        return false;
    }

    @ApiOperation(value = "", response = Boolean.class)
    @RequiresRoles("admin")
    @PostMapping("/forceResetPassword")
    public Boolean forceResetPassword() {
        logger.info("POST Request to /forceResetPassword");
        //todo
        return false;
    }

    @ApiOperation(value = "", response = EndUserVO.class)
    @RequiresRoles("admin")
    @GetMapping("/getUserList")
    public List<EndUserVO> getUserList() {
        logger.info("GET Request to /getUserList");

        List<EndUserVO> userVOList = new ArrayList<>();

        List<EndUserDTO> userDTOList = endUserService.queryUsers();
        BeanUtils.copyProperties(userDTOList, userVOList);

        userVOList.forEach(user -> {
            int id = user.getUserID();
            user.setRole(endUserService.getRoleByUserId(id));
            user.setPermissions(endUserService.getPermissionsByUserId(id));
        });

        return userVOList;
    }

    @ApiOperation(value = "", response = String.class)
    @RequiresRoles("admin")
    @GetMapping("/getAllRoles")
    public List<String> getAllRoles() {
        logger.info("GET Request to /getAllRoles");
        //todo
        return null;
    }

    @ApiOperation(value = "", response = String.class)
    @RequiresRoles("admin")
    @GetMapping("/getAllPermissions")
    public List<String> getAllPermissions() {
        logger.info("GET Request to /getAllPermissions");
        //todo
        return null;
    }
}
