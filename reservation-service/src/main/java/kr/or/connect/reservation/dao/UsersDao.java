package kr.or.connect.reservation.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import kr.or.connect.reservation.domain.Users;

@Repository
public class UsersDao {
	private NamedParameterJdbcTemplate jdbc; // sql 을 실행하기 위해 사용되는 객체
	private SimpleJdbcInsert insertAction; // insert 를 편리하게 하기 위한 객체

	private RowMapper<Users> rowMapper = BeanPropertyRowMapper.newInstance(Users.class);

	public UsersDao(DataSource dataSource) {
		this.jdbc = new NamedParameterJdbcTemplate(dataSource);
		this.insertAction = new SimpleJdbcInsert(dataSource).withTableName("users").usingGeneratedKeyColumns("id");
	}

	public Long insert(Users user) {
		SqlParameterSource params = new BeanPropertySqlParameterSource(user);
		return insertAction.executeAndReturnKey(params).longValue();
	}
	
	public Users selectUsersById(Long id) {
		Map<String, ?> params = Collections.singletonMap("id", id);
		return jdbc.queryForObject(UsersSqls.SELECT_USER_BY_ID, params, rowMapper);
	}

	public Long selectUserIdBySnsId(String snsId) {
		Map<String, Object> params = new HashMap<>();
		params.put("sns_id", snsId);

		Long exist = jdbc.queryForObject(UsersSqls.SELECT_EXIST_USER, params, Long.class);
		
		if(exist > 0) {
			exist = jdbc.queryForObject(UsersSqls.SELECT_USERID_BY_SNS_ID, params, Long.class);
		}
		
		return exist;
	}
}
