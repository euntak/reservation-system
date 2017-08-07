package kr.or.connect.reservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.connect.reservation.dao.FileDao;
import kr.or.connect.reservation.domain.FileDomain;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FileServiceImpl implements FileService{
	@Value("${spring.uploadfile.root-directory}")
	private String localDirectory;

	@Autowired
	private FileDao fileDao;

	@Transactional(readOnly = true)
	public FileDomain selectById(long id) {
		return fileDao.selectById(id);
	}

	@Transactional
	public List<Long> uploadFile(long userId, MultipartFile[] files)  {
		List<Long> fileIdList = new ArrayList<>();

		files.toString();

		String curtime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String savePath = localDirectory + File.separator + curtime;

		// 실제 저장될 폴더가 없으면 폴더 생성
		if (!new File(savePath).isDirectory()) {
			new File(savePath).mkdirs();
		}

		for (MultipartFile file : files) {
			String contentType = file.getContentType();
			String originalFilename = file.getOriginalFilename();
			long size = file.getSize();

			String uuid = UUID.randomUUID().toString();
			String saveFileName = savePath + File.separator + uuid;

				//db 등록
			FileDomain f = new FileDomain();
			f.setUserId(userId);
			f.setContentType(contentType);
			f.setFileName(originalFilename);
			f.setSaveFileName(saveFileName);
			f.setFileLength(size);
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
			Calendar cal = Calendar.getInstance();
			String now = formatter.format(cal.getTime());
			f.setCreateDate(Timestamp.valueOf(now));

			fileIdList.add(fileDao.insert(f));

			// try-with-resource 구문. close()를 할 필요가 없다. java 7 이상에서 가능
			try (InputStream in = file.getInputStream();
				 FileOutputStream fos = new FileOutputStream(saveFileName)) {
				int readCount = 0;
				byte[] buffer = new byte[512];

				while ((readCount = in.read(buffer)) != -1) {
					fos.write(buffer, 0, readCount);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return fileIdList;
	}
}
