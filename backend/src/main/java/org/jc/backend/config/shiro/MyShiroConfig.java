package org.jc.backend.config.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Indexed;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Indexed
@Configuration
public class MyShiroConfig {

    /**
     * shiro filter factory bean
     * @param mySecurityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager mySecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        shiroFilterFactoryBean.setSecurityManager(mySecurityManager);

        Map<String, String> urlFilterMap = new LinkedHashMap<>();
        urlFilterMap.put("/user", "anon");

        // swagger 2 config
//        urlFilterMap.put("/swagger-ui.html", "anon");
//        urlFilterMap.put("/swagger-resources/**", "anon");
//        urlFilterMap.put("/v2/**", "anon");
//        urlFilterMap.put("/webjars/springfox-swagger-ui/**", "anon");
//        urlFilterMap.put("/configuration/**", "anon");

        /* -------------- Production -------------- */
//        urlFilterMap.put("/**", "authc");
        /* -------------- Development -------------- */
        urlFilterMap.put("/**", "anon");

        shiroFilterFactoryBean.setLoginUrl("/user/userAuthentication");
//        shiroFilterFactoryBean.setUnauthorizedUrl("/user/userAuthorization");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(urlFilterMap);

        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("authc", new MyAuthenticationFilter());
        filterMap.put("roles", new MyAuthorizationFilter());

        shiroFilterFactoryBean.setFilters(filterMap);

        return shiroFilterFactoryBean;
    }

    /**
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("SHA-256");
        hashedCredentialsMatcher.setHashIterations(1);
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }

    /**
     * realm
     * @return
     */
    @Bean
    public MyShiroRealm userRealm() {
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }

    /**
     * default web security manager
     * @return
     */
    @Bean("mySecurityManager")
    public DefaultWebSecurityManager mySecurityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm());
        securityManager.setSessionManager(mySessionManager());
//        securityManager.setCacheManager(cacheManager());
        return securityManager;
    }

    /**
     * sessionManager
     * @return
     */
    @Bean
    public SessionManager mySessionManager() {
        MySessionManager mySessionManager = new MySessionManager();
        mySessionManager.setSessionIdUrlRewritingEnabled(false); //取消登陆跳转URL后面的jsessionid参数
//        mySessionManager.setSessionDAO(sessionDAO());
        mySessionManager.setGlobalSessionTimeout(-1);//不过期
        return mySessionManager;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

//    @Bean
//    public SimpleMappingExceptionResolver resolver() {
//        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
//        Properties properties = new Properties();
//        properties.setProperty("org.apache.shiro.authz.UnauthorizedException", "/403");
//        resolver.setExceptionMappings(properties);
//        return resolver;
//    }

//    @Bean
//    public EhCacheManager ehCacheManager() {
//        System.out.println("ShiroConfiguration.getEhCacheManager()");
//        EhCacheManager ehCacheManager = new EhCacheManager();
//        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
//        return ehCacheManager;
//    }



}
