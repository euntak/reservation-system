package kr.or.connect.reservation.service;

import kr.or.connect.reservation.domain.FileDomain;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

	public FileDomain selectById(long id);
	public List<Long> uploadFile(long userId, MultipartFile[] files) ;
}

