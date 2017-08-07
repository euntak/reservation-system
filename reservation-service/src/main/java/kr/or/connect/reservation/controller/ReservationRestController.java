package kr.or.connect.reservation.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpSession;

import kr.or.connect.reservation.anotation.AuthUser;
import kr.or.connect.reservation.domain.Reservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import kr.or.connect.reservation.domain.Users;
import kr.or.connect.reservation.dto.MyReservationDto;
import kr.or.connect.reservation.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationRestController {

    final Logger logger = LoggerFactory.getLogger(ReservationRestController.class);

    @Autowired
    ReservationService reservationService;
//
//    @Autowired
//    ProductService productService;

    @GetMapping
    public List<MyReservationDto> getMyReservation(HttpSession session) {
        Users user= (Users) session.getAttribute("loginedUser");
        return reservationService.selectReservation(user.getId());
    }

    @PutMapping
    public int updateReservaion(@RequestBody MyReservationDto reservation){
        return reservationService.updateReservaion(reservation);
    }

    @PostMapping
    public int insertReservation(@AuthUser Users users, @RequestBody Reservation reservation){
        reservation.setUserId(users.getId());

        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        Calendar cal = Calendar.getInstance();
        String now = formatter.format(cal.getTime());
        Timestamp ts = Timestamp.valueOf(now);

        reservation.setReservationDate(ts+"");
        reservation.setReservationType(1); // default setting

        logger.debug("reservation : {}", reservation);
        return reservationService.insertReservation(reservation);
        //insert가 안되면 에러 처리필요...
    }

}
