package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.EndUserO.EndUserLoginVO;
import org.jc.backend.entity.EndUserO.EndUserVO;
import org.jc.backend.entity.EndUserO.UserPermission;
import org.jc.backend.entity.EndUserO.UserRole;
import org.jc.backend.service.EndUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Indexed
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

    @ApiOperation(value = "user login api", response = EndUserVO.class)
    @RequiresGuest
    @PostMapping("/userAuthentication")
    public EndUserVO authenticate(@RequestBody @Validated EndUserLoginVO endUserLoginVO) {
        logger.info("POST Request to /user/userAuthentication");

        return endUserService.postUserLogInInfo(endUserLoginVO);
    }

    @ApiOperation(value = "user logout api")
    @RequiresAuthentication
    @PostMapping("/userLogout")
    public void logout() {
        logger.info("POST Request to /user/userLogout");
        endUserService.userLogout();
    }

    @ApiOperation(value = "", response = Boolean.class)
    @RequiresAuthentication
    @PostMapping("/changePassword")
    public Boolean changePassword(@RequestBody @Validated EndUserLoginVO endUserLoginVO) {
        logger.info("POST Request to /user/changePassword");

        //todo
        return false;
    }

    @ApiOperation(value = "", response = Boolean.class)
    @RequiresRoles("admin")
    @PostMapping("/forceResetPassword")
    public Boolean forceResetPassword() {
        logger.info("POST Request to /user/forceResetPassword");
        //todo
        return false;
    }

    @ApiOperation(value = "", response = String.class)
    @RequiresGuest
    @GetMapping("/getUserNameList")
    public List<String> getUserList() {
        logger.info("GET Request to /user/getUserNameList");

        return endUserService.queryUserNameList();
    }

    @ApiOperation(value = "", response = EndUserVO.class)
    @RequiresRoles("admin")
    @GetMapping("/getAllUsers")
    public List<EndUserVO> getAllUsers() {
        logger.info("GET Request to /user/getAllUsers");

        return endUserService.getAllUsers();
    }

    @ApiOperation(value = "", response = String.class)
    @RequiresRoles("admin")
    @GetMapping("/getAllRoles")
    public List<String> getAllRoles() {
        logger.info("GET Request to /user/getAllRoles");

        return endUserService.getAllRoles();
    }

    @ApiOperation(value = "", response = String.class)
    @RequiresRoles("admin")
    @GetMapping("/getAllPermissions")
    public List<String> getAllPermissions() {
        logger.info("GET Request to /user/getAllPermissions");

        return endUserService.getAllPermissions();
    }

    @ApiOperation(value = "", response = void.class)
    @RequiresRoles("admin")
    @PostMapping("/updateUser")
    public void updateUser(@RequestBody @Validated EndUserVO endUserVO) {
        logger.info("POST Request to /user/updateUser, info: {}", endUserVO);

        endUserService.updateUser(endUserVO);
    }

    @ApiOperation(value = "", response = void.class)
    @RequiresRoles("admin")
    @PutMapping("/createNewUser")
    public void createNewUser(@RequestBody @Validated EndUserVO endUserVO) {
        logger.info("PUT Request to /user/createNewUser, info: {}", endUserVO);

        endUserService.createUser(endUserVO);
    }

    @ApiOperation(value = "", response = void.class)
    @RequiresRoles("admin")
    @DeleteMapping("/deleteUser")
    public void deleteUser(@RequestParam("userID") int userID) throws GlobalParamException {
        logger.info("POST Request to /user/createNewUser, id: {}", userID);

        endUserService.deleteUser(userID);
    }
}
