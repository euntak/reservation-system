package kr.or.connect.reservation.service;

import kr.or.connect.reservation.domain.ReservationUserCommentImage;
import kr.or.connect.reservation.dto.CommentDto;
import kr.or.connect.reservation.dto.DetailProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.connect.reservation.dao.CommentDao;

import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

	@Autowired
	FileService fileService;

	@Autowired
	ProductService productService;

	@Autowired
	CommentDao commentDao;

	@Override
	@Transactional
	public Long insertComment(CommentDto comment)  {
		//리뷰 이미지 파일 업로드
		List<Long> fileIdList = fileService.uploadFile(comment.getUserId(),comment.getFiles());
		//리뷰 테이블 초기화
		long commentId = commentDao.insertComment(comment);
		//리뷰 이미지 테이블 초기화
		ReservationUserCommentImage commentImage = new ReservationUserCommentImage();
		commentImage.setReservationUserCommentId(commentId);

		for(int i=0; i < fileIdList.size(); i++){
			commentImage.setFileId(fileIdList.get(i));
			commentDao.insertCommentImage(commentImage);
		}

		return commentId;
	}

	@Override
	@Transactional(readOnly = true)
	public String selectProductById(long id) {
		DetailProductDto detailProductDto = (DetailProductDto)productService.getProductById(id).get("product");
		return detailProductDto.getName();
	}
	
}
