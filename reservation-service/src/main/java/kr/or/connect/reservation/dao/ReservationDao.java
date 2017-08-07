package kr.or.connect.reservation.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import kr.or.connect.reservation.domain.Reservation;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import kr.or.connect.reservation.dto.MyReservationDto;

@Repository
public class ReservationDao {
    private NamedParameterJdbcTemplate jdbc;
    private SimpleJdbcInsert insertAction;
    private RowMapper<MyReservationDto> reservationMapper = BeanPropertyRowMapper.newInstance(MyReservationDto.class);

    public ReservationDao(DataSource dataSource) {
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        this.insertAction=new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_info").usingGeneratedKeyColumns("id");
    }

    public List<MyReservationDto> selectReservation(long userId){
        Map<String,?> params= Collections.singletonMap("id", userId);
        return jdbc.query(ReservationSqls.SELECT_RESERVATION, params,reservationMapper);
    }

    public int updateReservation(long id, int type) {
        Map<String,Object> params = new HashMap();
        params.put("id", id);
        params.put("type",type);
        params.put("cancel_type", 4);
        return jdbc.update(ReservationSqls.UPDATE_RESERVATION, params);
    }

    public int insertReservation(Reservation reservation) {
        SqlParameterSource params=new BeanPropertySqlParameterSource(reservation);
        return insertAction.executeAndReturnKey(params).intValue();
    }
}
