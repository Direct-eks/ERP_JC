package org.jc.backend.config.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyAuthorizationFilter extends RolesAuthorizationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//        httpServletResponse.setContentType("application/json; charset=utf-8");
        Subject subject = getSubject(request, response);
        if (subject.getPrincipal() == null) {
            httpServletResponse.setStatus(401);
        }
        else {
            httpServletResponse.setStatus(403);
        }
        httpServletResponse.getWriter().flush();
        return false;
    }
}
