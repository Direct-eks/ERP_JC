package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.jc.backend.dao.EndUserMapper;
import org.jc.backend.entity.EndUserO.*;
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
    @Override
    public EndUserDO getUserByName(String username) {
        EndUserDO userDO = new EndUserDO();
        BeanUtils.copyProperties(endUserMapper.queryUserByName(username), userDO);
        return userDO;
    }

    @Transactional(readOnly = true)
    @Override
    public String getRoleByUserId(int id) {
        try {
            return endUserMapper.queryRoleByUserId(id).getRole();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> getPermissionsByUserId(int id) {
        try {
            List<String> list = new ArrayList<>();
            endUserMapper.queryPermissionsByUserId(id).forEach(p -> list.add(p.getPermission()));
            return list;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Override
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

    @Override
    public void userLogout() {
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipals().getPrimaryPrincipal();
        logger.info("User logging out: " + username);
        subject.logout();
        logger.info("User logged out: " + username);
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> queryUserNameList() {
        try {
            List<String> userList = new ArrayList<>();
            List<EndUserDO> usersFromDatabase = endUserMapper.queryAllUsers();

            usersFromDatabase.forEach(user -> userList.add(user.getUsername()));
            return userList;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<EndUserVO> getAllUsers() {
        try {
            List<EndUserVO> userList = new ArrayList<>();

            List<EndUserDO> usersFromDatabase = endUserMapper.queryAllUsers();
            for (var user : usersFromDatabase) {
                EndUserVO userVO = new EndUserVO();
                BeanUtils.copyProperties(user, userVO);
                int id = userVO.getUserID();
                userVO.setRole(endUserMapper.queryRoleByUserId(id));
                userVO.setPermissions(endUserMapper.queryPermissionsByUserId(id));

                userList.add(userVO);
            }
            return userList;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserRole> getAllRoles() {
        try {
            return endUserMapper.queryAllRoles();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserPermission> getAllPermissions() {
        try {
            return endUserMapper.queryAllPermissions();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

}
