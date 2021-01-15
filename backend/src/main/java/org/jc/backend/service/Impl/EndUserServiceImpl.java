package org.jc.backend.service.Impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.jc.backend.dao.EndUserMapper;
import org.jc.backend.entity.DO.EndUserDO;
import org.jc.backend.entity.VO.EndUserLoginVO;
import org.jc.backend.entity.VO.EndUserVO;
import org.jc.backend.service.EndUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EndUserServiceImpl implements EndUserService {
    private static final Logger logger = LoggerFactory.getLogger(EndUserServiceImpl.class);

    private final EndUserMapper endUserMapper;

    public EndUserServiceImpl(EndUserMapper endUserMapper) {
        this.endUserMapper = endUserMapper;
    }

    public EndUserDO getUserByName(String username) {
        EndUserDO userDTOList = new EndUserDO();
        BeanUtils.copyProperties(endUserMapper.queryUserByName(username), userDTOList);
        return userDTOList;
    }

    public String getRoleByUserId(int id) {
        return endUserMapper.queryRolesByUserId(id);
    }

    public List<String> getPermissionsByUserId(int id) {
        return endUserMapper.queryPermissionsByUserId(id);
    }

    public String postUserLogInInfo(EndUserLoginVO user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        subject.login(token);
        logger.info("User logging in: " + user.getUsername());
        Session session = subject.getSession();
        logger.info("User logged in: " + user.getUsername());
        return session.getId().toString();
    }

    public void userLogout() {
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipals().getPrimaryPrincipal();
        logger.info("User logging out: " + username);
        subject.logout();
        logger.info("User logged out: " + username);
    }


    public List<EndUserVO> queryUsers() {
        //todo
        return null;
    }
}
