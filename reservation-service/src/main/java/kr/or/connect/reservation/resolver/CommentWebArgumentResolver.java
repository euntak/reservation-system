package kr.or.connect.reservation.resolver;

import kr.or.connect.reservation.anotation.AnoComment;
import kr.or.connect.reservation.dto.CommentDto;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommentWebArgumentResolver implements HandlerMethodArgumentResolver{
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        AnoComment comment = parameter.getParameterAnnotation(AnoComment.class);
        if(comment == null)
            return false;
        else
            return true;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        AnoComment comment = parameter.getParameterAnnotation(AnoComment.class);
        if(comment == null){
            return WebArgumentResolver.UNRESOLVED;
        }

        CommentDto commentDto = new CommentDto();
        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();

        commentDto.setComment(request.getParameter("comment"));
        commentDto.setScore(Double.parseDouble(request.getParameter("score")));
        commentDto.setProductId(Long.parseLong(nativeWebRequest.getParameter("productId")));

        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        Calendar cal = Calendar.getInstance();
        String now = formatter.format(cal.getTime());
        Timestamp ts = Timestamp.valueOf(now);
        commentDto.setCreateDate(ts+"");
        commentDto.setModifyDate(ts+"");

        return commentDto;


    }
}
