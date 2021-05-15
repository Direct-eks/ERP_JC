package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
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
import org.springframework.util.StringUtils;

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
            return endUserMapper.queryRoleByUserId(id);

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
            return endUserMapper.queryPermissionsByUserId(id);

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
    public List<String> getAllRoles() {
        try {
            List<String> list = new ArrayList<>();
            endUserMapper.queryAllRoles().forEach(r -> list.add(r.getRole()));
            return list;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> getAllPermissions() {
        try {
            List<String> list = new ArrayList<>();
            endUserMapper.queryAllPermissions().forEach(p -> list.add(p.getPermission()));
            return list;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateUser(EndUserVO endUserVO) {
        try {
            // update user info (username & remark)
            int id = endUserVO.getUserID();
            EndUserDO userDO = new EndUserDO();
            BeanUtils.copyProperties(endUserVO, userDO);
            endUserMapper.updateUserInfo(userDO);

            // check for changed role
            List<UserRole> allRoles = endUserMapper.queryAllRoles();
            String oldUserRole = endUserMapper.queryRoleByUserId(id);
            String newUserRole = endUserVO.getRole();
            if (!oldUserRole.equals(newUserRole)) {
                int roleID = -1;
                for (var r : allRoles) {
                    if (r.getRole().equals(newUserRole)) {
                        roleID = r.getRoleID();
                        break;
                    }
                }
                if (roleID == -1) throw new PersistenceException();
                endUserMapper.updateUserRole(id, roleID);
            }

            // check for permissions changes
            List<UserPermission> allPermissions = endUserMapper.queryAllPermissions();
            List<String> oldUserPermissions = endUserMapper.queryPermissionsByUserId(id);
            List<String> newUserPermissions = endUserVO.getPermissions();
            // check for added
            List<String> tempNewPermissions = new ArrayList<>(newUserPermissions);
            tempNewPermissions.removeAll(oldUserPermissions);
            for (var p : tempNewPermissions) {
                int permissionID = -1;
                for (var per : allPermissions) {
                    if (per.getPermission().equals(p)) {
                        permissionID = per.getPermissionID();
                        break;
                    }
                }
                if (permissionID == -1) throw new PersistenceException();
                endUserMapper.insertUserPermission(id, permissionID);
            }

            // check for removed
            List<String> tempOldPermissions = new ArrayList<>(oldUserPermissions);
            tempOldPermissions.removeAll(newUserPermissions);
            for (var p : tempOldPermissions) {
                int permissionID = -1;
                for (var per : allPermissions) {
                    if (per.getPermission().equals(p)) {
                        permissionID = per.getPermissionID();
                        break;
                    }
                }
                if (permissionID == -1) throw new PersistenceException();
                endUserMapper.deleteUserPermission(id, permissionID);
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void createUser(EndUserVO endUserVO) {
        try {
            EndUserDO userDO = new EndUserDO();
            BeanUtils.copyProperties(endUserVO, userDO);
            if (!StringUtils.hasLength(userDO.getPassword())) throw new PersistenceException();
            String hashed = new Sha256Hash(userDO.getPassword()).toString();
            userDO.setPassword(hashed);
            endUserMapper.insertUserInfo(userDO);
            int id = userDO.getUserID();
            logger.info("created user with id: " + id);

            List<UserRole> allRoles = endUserMapper.queryAllRoles();
            String role = endUserVO.getRole();
            int roleID = -1;
            for (var r : allRoles) {
                if (r.getRole().equals(role)) {
                    roleID = r.getRoleID();
                    break;
                }
            }
            if (roleID == -1) throw new PersistenceException();
            endUserMapper.insertUserRole(id, roleID);

            List<UserPermission> allPermissions = endUserMapper.queryAllPermissions();
            List<String> userPermissions = endUserVO.getPermissions();
            for (var p : userPermissions) {
                int permissionID = -1;
                for (var per : allPermissions) {
                    if (per.getPermission().equals(p)) {
                        permissionID = per.getPermissionID();
                        break;
                    }
                }
                if (permissionID == -1 ) throw new PersistenceException();
                endUserMapper.insertUserPermission(id, permissionID);
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Override
    public void deleteUser(int id) {
        try {
            endUserMapper.deleteUserInfo(id);
            endUserMapper.deleteUserRole(id);
            List<UserPermission> allPermissions = endUserMapper.queryAllPermissions();
            for (var p : allPermissions) {
                endUserMapper.deleteUserPermission(id, p.getPermissionID());
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }
}
