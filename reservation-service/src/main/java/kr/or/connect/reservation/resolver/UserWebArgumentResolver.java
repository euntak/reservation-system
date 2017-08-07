package kr.or.connect.reservation.resolver;

import kr.or.connect.reservation.anotation.AuthUser;
import kr.or.connect.reservation.common.SecurityContext;
import kr.or.connect.reservation.domain.Users;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UserWebArgumentResolver implements HandlerMethodArgumentResolver{

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //System.out.println("supportsParameter!!!!!!!!!!!!!!!");

        AuthUser loginedUser = parameter.getParameterAnnotation(AuthUser.class);
        if(loginedUser == null)
            return false;
        else
            return true;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        //System.out.println("resolveArgument!!!!!!!!!!!!!!!!!!!");
        AuthUser loginedUser = parameter.getParameterAnnotation(AuthUser.class);

        if(loginedUser == null){
            SecurityContext.loginUser.set(null);
            return WebArgumentResolver.UNRESOLVED;
        }
//
//        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
//        HttpSession session = request.getSession();
//        Users user = null;
//        if(session != null || session.getAttribute("loginedUser") != null) {
//            user = (Users) session.getAttribute("loginedUser");
//        }
        return SecurityContext.loginUser.get();
    }
}
