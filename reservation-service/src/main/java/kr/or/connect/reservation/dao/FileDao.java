package kr.or.connect.reservation.dao;

import java.util.Collections;
import java.util.Map;

import javax.sql.DataSource;

import kr.or.connect.reservation.utils.FileUploadException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import kr.or.connect.reservation.domain.FileDomain;

@Repository
public class FileDao {
	private NamedParameterJdbcTemplate jdbc; // sql 을 실행하기 위해 사용되는 객체
	private SimpleJdbcInsert insertAction; // insert 를 편리하게 하기 위한 객체

	private RowMapper<FileDomain> rowMapper = BeanPropertyRowMapper.newInstance(FileDomain.class);

	public FileDao(DataSource dataSource) {
		this.jdbc = new NamedParameterJdbcTemplate(dataSource);
		this.insertAction = new SimpleJdbcInsert(dataSource).withTableName("file").usingGeneratedKeyColumns("id");
	}

	public Long insert(FileDomain File)  {
		SqlParameterSource params = new BeanPropertySqlParameterSource(File);
		return insertAction.executeAndReturnKey(params).longValue();
	}
	
	public FileDomain selectById(long id) {
		Map<String, ?> params = Collections.singletonMap("id", id);

		FileDomain file = jdbc.queryForObject(FileSqls.SELECT_BY_ID, params, rowMapper);

		if(file == null) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return file;
	}

}
