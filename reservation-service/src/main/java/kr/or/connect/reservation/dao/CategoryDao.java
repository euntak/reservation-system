package kr.or.connect.reservation.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import kr.or.connect.reservation.domain.Category;

@Repository
public class CategoryDao {
	private NamedParameterJdbcTemplate jdbc; // sql 을 실행하기 위해 사용되는 객체

	private SimpleJdbcInsert insertAction; // insert 를 편리하게 하기 위한 객체

	private RowMapper<Category> rowMapper = BeanPropertyRowMapper.newInstance(Category.class);

	public CategoryDao(DataSource dataSource) {
		this.jdbc = new NamedParameterJdbcTemplate(dataSource);
		this.insertAction = new SimpleJdbcInsert(dataSource).withTableName("category").usingGeneratedKeyColumns("id");
	}

	public Long insert(Category category) {
		SqlParameterSource params = new BeanPropertySqlParameterSource(category);
		return insertAction.executeAndReturnKey(params).longValue();
	}
	public Category selectById(long id) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		
		Category category = jdbc.queryForObject(CategorySqls.SELECT_BY_ID, params, rowMapper);
		
		if(category == null) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return category;
	}

	public List<Category> selectAll() {
		Map<String, Object> params = Collections.emptyMap();
		return jdbc.query(CategorySqls.SELECT_ALL, params, rowMapper);
	}

	public int update(Category category) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", category.getId());
		params.put("name", category.getName());
		return jdbc.update(CategorySqls.UPDATE_BY_ID, params);
	}

	public int delete(Long id) {
		Map<String, ?> params = Collections.singletonMap("id", id);
		return jdbc.update(CategorySqls.DELETE_BY_ID, params);
	}
}
