package kr.or.connect.reservation.dao;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import kr.or.connect.reservation.dto.CommentDto;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import kr.or.connect.reservation.domain.FileDomain;
import kr.or.connect.reservation.domain.ReservationUserComment;
import kr.or.connect.reservation.domain.ReservationUserCommentImage;

@Repository
public class CommentDao {
	
	private NamedParameterJdbcTemplate jdbc; 
	private SimpleJdbcInsert commentInsertAction;
	private SimpleJdbcInsert commentImageInsertAction;
	
	private RowMapper<FileDomain> rowMapper = BeanPropertyRowMapper.newInstance(FileDomain.class);

	public CommentDao(DataSource dataSource) {
		this.jdbc = new NamedParameterJdbcTemplate(dataSource);
		this.commentInsertAction = new SimpleJdbcInsert(dataSource).withTableName("reservation_user_comment").usingGeneratedKeyColumns("id");
		this.commentImageInsertAction = new SimpleJdbcInsert(dataSource).withTableName("reservation_user_comment_image").usingGeneratedKeyColumns("id");
	}

	public Long insertComment(CommentDto comment) {
		SqlParameterSource params = new BeanPropertySqlParameterSource(comment);
		return commentInsertAction.executeAndReturnKey(params).longValue();
	}

	public Long insertCommentImage(ReservationUserCommentImage commentImage) {
		SqlParameterSource params = new BeanPropertySqlParameterSource(commentImage);
		return commentImageInsertAction.executeAndReturnKey(params).longValue();
	}

	public int updateCommentImageFileStatus(Long fileId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fileId", fileId);
		return jdbc.update(CommentSqls.updateCommentImageFileStatus, params);
	}
}
