package kr.or.connect.reservation.service;

import java.util.List;

import kr.or.connect.reservation.domain.Reservation;
import kr.or.connect.reservation.dto.MyReservationDto;

public interface ReservationService {
    public List<MyReservationDto> selectReservation(long userId);
    public int updateReservaion(MyReservationDto reservation);

    public int insertReservation(Reservation reservation);

}
