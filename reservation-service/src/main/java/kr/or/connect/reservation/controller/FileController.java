package kr.or.connect.reservation.controller;

import kr.or.connect.reservation.domain.FileDomain;
import kr.or.connect.reservation.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;

@Controller
@RequestMapping("/files")
public class FileController {

	@Value("${spring.uploadfile.root-directory}")
	private String localDirectory;

	@Autowired
	private FileService fileService;

	@GetMapping
	public String fileform() {
		return "files";
	}


	@GetMapping(path = "/{id}")
	public void downloadFile(
			@PathVariable(name = "id") long id, 
			HttpServletResponse response) {

		FileDomain f = fileService.selectById(id);

		long fileSize = f.getFileLength();
		String contentType = f.getContentType();
		String saveFileName = f.getSaveFileName();
		String originalFilename = f.getSaveFileName();

		response.setHeader("Content-Disposition", "attachment; filename=\"" + originalFilename + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Type", contentType);
		//response.setHeader("Content-Length", "" + fileSize);

		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");

		java.io.File readFile = new java.io.File(saveFileName);
		if (!readFile.exists()) { // 파일이 존재하지 않다면
			throw new RuntimeException("file not found");
		}

		FileInputStream fis = null;

		try {
			fis = new FileInputStream(readFile);
			FileCopyUtils.copy(fis, response.getOutputStream()); // 파일을 저장할때도 사용할 수 있다.
			response.getOutputStream().flush();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				fis.close();
			} catch (Exception ex) {
				// 아무것도 하지 않음 (필요한 로그를 남기는 정도의 작업만 함.)
			}
		}
	}
}
