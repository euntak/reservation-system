package kr.or.connect.reservation.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import kr.or.connect.reservation.utils.FileUploadException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import kr.or.connect.reservation.config.RootApplicationContextConfig;
import kr.or.connect.reservation.domain.FileDomain;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootApplicationContextConfig.class)
/*@Transactional*/
public class FileDaoTest {

	@Autowired
	FileDao dao;

	//@Test
//	public void shouldselectFileTest() {
//
//		FileDomain file = new FileDomain();
//		file.setContentType("contentType");
//		file.setFileName("fileName");
//		file.setSaveFileName("saveFileName");
//		file.setUserId(1);
//		file.setDeleteFlag(0);
//		file.setFileLength(123);
//
//		long returnKey = 0L;
//		try {
//			returnKey = dao.insert(file);
//		} catch (FileUploadException fe) {
//			System.out.println("에러 메세지 : "+fe.getMessage());
//			fe.printStackTrace();
//		}
//		FileDomain result = dao.selectById(returnKey);
//
//		assertNotNull(result);
//		assertThat(returnKey, is(result.getId()));
//
//	}

//	@Test
//	public void shouldinsertFileTest() {
//		FileDomain file = new FileDomain();
//		file.setContentType("contentType");
//		file.setFileName("fileName");
//		file.setSaveFileName("saveFileName");
//		file.setUserId(1);
//		file.setDeleteFlag(0);
//		file.setFileLength(123);
//
//		long returnKey = 0L;
//		try {
//			returnKey = dao.insert(file);
//		} catch (FileUploadException fe) {
//			System.out.println("에러 메세지 : "+fe.getMessage());
//			fe.printStackTrace();
//		}
//		FileDomain result = dao.selectById(returnKey);
//
//		assertThat(result.getFileName(), is("fileName"));
//	}
	
	@Test
	@Transactional(rollbackFor = FileUploadException.class)
	public void shouldinsertCommentFileTest() {
		FileDomain file1 = new FileDomain();
		file1.setContentType("image/jpeg");
		file1.setFileName("images2.jpeg");
		file1.setSaveFileName("C:/boost/uploadFiles/2017-07-19/9971cd9e-1a67-4281-9261-762137f02293");
		file1.setUserId(1);
		file1.setDeleteFlag(0);
		file1.setFileLength(5774);
		
		FileDomain file2 = new FileDomain();
		file2.setContentType("image/jpeg");
		file2.setFileName("images2.jpeg");
		file2.setSaveFileName("C:/boost/uploadFiles/2017-07-19/9971cd9e-1a67-4281-9261-762137f02293");
		file2.setUserId(1);
		file2.setDeleteFlag(0);
		file2.setFileLength(5774);
		
		FileDomain file3 = new FileDomain();
		file3.setContentType("image/jpeg");
		file3.setFileName("images2.jpeg");
		file3.setSaveFileName("C:/boost/uploadFiles/2017-07-19/9971cd9e-1a67-4281-9261-762137f02293");
		file3.setUserId(1);
		file3.setDeleteFlag(0);
		file3.setFileLength(5774);
		
		FileDomain[] files = { file1, file2, file3 };
		
		long returnKey = 0L;
		for(int i=0; i<files.length; i++) {
			try {
				//System.out.println(i+ " 번 초");
				//returnKey = dao.insertCommentImageFile(files[i]);
				if(i==1) {
					throw new FileUploadException("업로드중 에러 발생!!");
				}
			} catch (FileUploadException fe) {
				//System.out.println(i+" 번 중");
				//System.out.println("에러 메세지 : "+fe.getMessage());
				fe.printStackTrace();
				//System.out.println("업로드 중 에러 발생으로 모두 롤백 처리됩니다");
				break;
			}
			FileDomain result = dao.selectById(returnKey);
			//System.out.println(i+" 번 말");
			assertThat(result.getFileName(), is("images2.jpeg"));
		}
	}
}
