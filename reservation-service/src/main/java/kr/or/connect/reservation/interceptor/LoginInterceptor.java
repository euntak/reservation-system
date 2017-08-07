package kr.or.connect.reservation.interceptor;

import kr.or.connect.reservation.common.SecurityContext;
import kr.or.connect.reservation.domain.Users;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        Users users = (Users) session.getAttribute("loginedUser");

        if(users != null){
            SecurityContext.loginUser.set(users);
        }else{
            request.getSession().setAttribute("prevPage",request.getServletPath());
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}
