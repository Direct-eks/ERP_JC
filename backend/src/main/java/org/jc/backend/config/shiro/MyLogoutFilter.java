package org.jc.backend.config.shiro;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;

public class MyLogoutFilter extends LogoutFilter {

//    @Override
//    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
//        Subject subject = getSubject(request, response);
//        try {
//            subject.logout();
//        } catch (SessionException ise) {
////            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
//        }
//        response.setCharacterEncoding("UTF-8");
//        PrintWriter out = response.getWriter();
//        JSONObject result = new JSONObject();
//        result.put("code", 0);
//        result.put("msg", "成功");
//        out.println(result);
//        out.flush();
//        out.close();
//        return false;
//    }
}