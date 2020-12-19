package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.entity.DO.EndUserDO;
import org.jc.backend.service.EndUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "User Related")
@RestController
@RequestMapping("/user")
public class EndUserController {

    private static final Logger logger = LoggerFactory.getLogger(EndUserController.class);

    private final EndUserService endUserService;

    public EndUserController(EndUserService endUserService) {
        this.endUserService = endUserService;
    }

    @ApiOperation(value = "user login api", response = String.class)
    @PostMapping("/userAuthentication")
    public String authenticate(@RequestBody @Validated EndUserDO endUserDO) {
        logger.info("POST Request to /userAuthentication");
        return endUserService.postUserLogInInfo(endUserDO);
    }

    @ApiOperation(value = "user logout api")
    @PostMapping("/userLogout")
    public void logout() {
        logger.info("POST Request to /userLogout");
        endUserService.userLogout();
    }
}
