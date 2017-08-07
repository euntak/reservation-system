package kr.or.connect.reservation.service;

import java.util.List;

import kr.or.connect.reservation.domain.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.connect.reservation.dao.ReservationDao;
import kr.or.connect.reservation.dto.MyReservationDto;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationServiceImpl implements ReservationService {
	@Autowired
	ReservationDao reservationDao;

	@Transactional(readOnly = true)
	@Override
	public List<MyReservationDto> selectReservation(long userId) {
		List<MyReservationDto> list = reservationDao.selectReservation(userId);
		for (MyReservationDto dto : list) {
			int totoalPrice = dto.getGeneralTotalPrice() + dto.getYouthTotalPrice() + dto.getChildTotalPrice();
			dto.setTotalPrice(totoalPrice);
		}
		return list;
	}

	@Transactional
	@Override
	public int updateReservaion(MyReservationDto reservation) {
		long id = reservation.getRid();
		int type = reservation.getReservationType();
		return reservationDao.updateReservation(id, type);
	}

	@Transactional
	@Override
	public int insertReservation(Reservation reservation) {
		return reservationDao.insertReservation(reservation);
	}

}
