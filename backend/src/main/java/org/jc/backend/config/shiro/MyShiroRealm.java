package org.jc.backend.config.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.jc.backend.entity.EndUserO.EndUserDO;
import org.jc.backend.service.EndUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;


public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    EndUserService userService;

    private static final Logger logger = LoggerFactory.getLogger(MyShiroRealm.class);

    /**
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken userToken = (UsernamePasswordToken) token;

        EndUserDO user = userService.getUserByName(userToken.getUsername());

        if (user == null) {
            throw new AuthenticationException("no such account");
        }

//        if(!user.getPassword().equals(new String(userToken.getPassword()))){
//            throw new IncorrectCredentialsException("账户密码不正确");
//        }

        Subject subject = SecurityUtils.getSubject();


        return new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), "");


//        System.out.println("MyShiroRealm.doGetAuthenticationInfo()");
        //获取用户的输入的账号.
//        String username = (String) token.getPrincipal();
//        System.out.println(token.getCredentials());
        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
//        User userInfo = User.findByUsername(username);
//        System.out.println("----->>userInfo="+userInfo);
//        if (userInfo == null) {
//            return null;
//        }
//        if (userInfo.getState() == 1) { //账户冻结
//            throw new LockedAccountException();
//        }
//        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
//                userInfo, //用户名
//                userInfo.getPassword(), //密码
//                ByteSource.Util.bytes(userInfo.getCredentialsSalt()),//salt=username+salt
//                getName()  //realm name
//        );
//        return authenticationInfo;
    }

    /**
     * 权限验证
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        logger.info("Authorizing...");
        String username = (String) principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        EndUserDO user = userService.getUserByName(username);

        HashSet<String> roles = new HashSet<>();
        roles.add(userService.getRoleByUserId(user.getUserID()));
        authorizationInfo.setRoles(roles);

        var permissions = userService.getPermissionsByUserId(user.getUserID());
        authorizationInfo.addStringPermissions(permissions);

        SecurityUtils.getSubject().getSession().setAttribute("permissions", permissions);
        SecurityUtils.getSubject().getSession().setAttribute("role", roles);

        return authorizationInfo;
    }


}
