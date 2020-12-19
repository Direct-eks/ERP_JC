package org.jc.backend.config.shiro;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Override default shiro setting of redirection
 */
public class MyAuthenticationFilter extends FormAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(MyAuthenticationFilter.class);

    /**
     *
     * @param servletRequest
     * @param servletResponse
     * @param o
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(servletRequest);
        if ("OPTIONS".equals(httpServletRequest.getMethod())) {
            return true;
        }
        return super.isAccessAllowed(servletRequest, servletResponse, o);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                logger.info("Login submission detected.  Attempting to execute login.");
                return executeLogin(request, response);
            }
            else {
                logger.info("Login page view.");
                // allow them to see the login page
                return true;
            }
        }
        else { // 若不是
            logger.info("Attempting to access a path which requires authentication." +
                    "Forwarding...");
            // 重定向
//            saveRequestAndRedirectToLogin(request, response);
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(401);
            httpServletResponse.getWriter().flush();
            return false;
        }


//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

//        Subject subject = SecurityUtils.getSubject();
//        Object user = subject.getPrincipal();
//
//        if (Objects.equals(user, null)) {
//            Map<String, Object> result = new LinkedHashMap<>();
//            result.put("code", 101);
//            result.put("msg", "未登录");
//            result.put("data",null);
//            httpServletResponse.setCharacterEncoding("UTF-8");
//            httpServletResponse.setContentType("application/json");
//            httpServletResponse.getWriter().write();
//            httpServletResponse.setStatus(401);
//        }

//        httpServletResponse.setContentType("application/json; charset=utf-8");
//        httpServletResponse.setStatus(401);
//        httpServletResponse.getWriter().flush();
//        return false;
    }

}
