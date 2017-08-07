package kr.or.connect.reservation.service;

import kr.or.connect.reservation.dto.CommentDto;

public interface CommentService {
	public Long insertComment(CommentDto comment) ;
	public String selectProductById(long id);
}
