package kr.or.connect.reservation.common;

import kr.or.connect.reservation.domain.Users;

public class SecurityContext {
    public static ThreadLocal<Users> loginUser = new ThreadLocal<Users>();
}
