package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.jc.backend.dao.EndUserMapper;
import org.jc.backend.entity.EndUserO.EndUserDO;
import org.jc.backend.entity.EndUserO.EndUserLoginVO;
import org.jc.backend.entity.EndUserO.EndUserVO;
import org.jc.backend.service.EndUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class EndUserServiceImpl implements EndUserService {
    private static final Logger logger = LoggerFactory.getLogger(EndUserServiceImpl.class);

    private final EndUserMapper endUserMapper;

    public EndUserServiceImpl(EndUserMapper endUserMapper) {
        this.endUserMapper = endUserMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional(readOnly = true)
    public EndUserDO getUserByName(String username) {
        EndUserDO userDO = new EndUserDO();
        BeanUtils.copyProperties(endUserMapper.queryUserByName(username), userDO);
        return userDO;
    }

    @Transactional(readOnly = true)
    public String getRoleByUserId(int id) {
        return endUserMapper.queryRoleByUserId(id);
    }

    @Transactional(readOnly = true)
    public List<String> getPermissionsByUserId(int id) {
        return endUserMapper.queryPermissionsByUserId(id);
    }

    public EndUserVO postUserLogInInfo(EndUserLoginVO user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        subject.login(token);
        logger.info("User logging in: " + user.getUsername());
        Session session = subject.getSession();
        logger.info("User logged in: " + user.getUsername());

        EndUserDO userFromDatabase = getUserByName(user.getUsername());
        EndUserVO endUser = new EndUserVO();
        BeanUtils.copyProperties(userFromDatabase, endUser);
        int userID = endUser.getUserID();
        endUser.setSessionID(session.getId().toString());
        endUser.setPermissions(endUserMapper.queryPermissionsByUserId(userID));
        endUser.setRole(endUserMapper.queryRoleByUserId(userID));

        return endUser;
    }

    public void userLogout() {
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipals().getPrimaryPrincipal();
        logger.info("User logging out: " + username);
        subject.logout();
        logger.info("User logged out: " + username);
    }

    @Transactional(readOnly = true)
    public List<String> queryUserNameList() {

        List<String> userList = new ArrayList<>();

        try {
            List<EndUserDO> usersFromDatabase = endUserMapper.queryAllUsers();

            usersFromDatabase.forEach(user -> userList.add(user.getUsername()));

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production
            logger.error("query failed");
            throw e;
        }

        return userList;
    }
}
