package kr.or.connect.reservation.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import kr.or.connect.reservation.config.RootApplicationContextConfig;
import kr.or.connect.reservation.dto.MyReservationDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootApplicationContextConfig.class)
@Transactional
public class ReservationDaoTest {
	
	@Autowired
	private ReservationDao dao;

	@Test
	public void shouldGetReservation() {
		Long userId = 1L;
		List<MyReservationDto> result = dao.selectReservation(userId);
		assertEquals(4, result.size());

		for(MyReservationDto dto : result) {
			assertNotNull(dto);
		}
	}
	
	@Test
	public void updateReservation() {
		Long id = 1L;
		int type = 1;
		int result = dao.updateReservation(id, type);
		assertEquals(1, result);
	}
}
