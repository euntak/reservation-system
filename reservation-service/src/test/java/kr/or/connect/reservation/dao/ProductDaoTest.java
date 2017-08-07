package kr.or.connect.reservation.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import kr.or.connect.reservation.config.RootApplicationContextConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootApplicationContextConfig.class)
@Transactional
public class ProductDaoTest {

	@Autowired
	ProductDao dao;

	@Test
	public void shouldGetAllProducts() {
		int limit = 2;
		int offset = 0;

		Map<String, Object> result = dao.selectAllProduct(limit, offset);
		assertNotNull(result);
		assertThat(result.size(), is(limit));
	}

	@Test
	public void shouldGetProductById() {
		long id = 1;
		Map<String, Object> detailProduct = dao.selectProductById(id);
		assertNotNull("DetailProduct is null.", detailProduct);
	}

	@Test
	public void shouldgetProductComments() {
		long id = 1;
		int limit = 3;
		int offset = 0;
		Map<String, Object> comments = dao.selectProductPreviewComments(id, limit, offset);
		assertNotNull("Comments is null.", comments);
	}

	@Test
	public void shouldGetProductsByCategoryId() {
		long categoryId = 1;
		int limit = 2;
		int offset = 0;

		Map<String, Object> result = dao.selectProductsByCategoryId(categoryId, limit, offset);
		assertNotNull("Product List is null.", result);
		assertThat(result.size(), is(limit));
	}

}
