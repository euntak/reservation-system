package kr.or.connect.reservation.anotation;


import java.lang.annotation.*;

@Target(ElementType.PARAMETER) // 파라미터에 붙는 어노테이션이라는 것을 의미
@Retention(RetentionPolicy.RUNTIME) //동적으로 해당 어노테이션을 사용하기위해서
@Documented // 문서에도 어노테이션의 정보가 표현된다는 것을 의미
public @interface AuthUser {
}
