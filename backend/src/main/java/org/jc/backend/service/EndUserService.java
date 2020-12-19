package org.jc.backend.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.jc.backend.dao.EndUserMapper;
import org.jc.backend.entity.DO.EndUserDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EndUserService {

    private static final Logger logger = LoggerFactory.getLogger(EndUserService.class);

    private final EndUserMapper endUserMapper;

    public EndUserService(EndUserMapper endUserMapper) {
        this.endUserMapper = endUserMapper;
    }

    public EndUserDO queryUserByName(String username) {
        return endUserMapper.queryUserByName(username);
    }

    public List<String> queryRolesByUserId(int id) {
        return endUserMapper.queryRolesByUserId(id);
    }

    public List<String> queryPermissionsByUserId(int id) {
        return endUserMapper.queryPermissionsByUserId(id);
    }

    public String postUserLogInInfo(EndUserDO user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        subject.login(token);
        logger.info("user logged in: " + user.getUsername());
        Session session = subject.getSession();
        return session.getId().toString();
    }

    public void userLogout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        logger.info("user logged out");
    }
}
