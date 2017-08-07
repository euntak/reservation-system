package kr.or.connect.reservation.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import kr.or.connect.reservation.domain.Price;
import kr.or.connect.reservation.dto.CommentTotalValueDto;
import kr.or.connect.reservation.dto.DetailProductDto;
import kr.or.connect.reservation.dto.ImageDto;
import kr.or.connect.reservation.dto.MainPageProductDto;
import kr.or.connect.reservation.dto.PreviewCommentDto;

@Repository
public class ProductDao {
	private NamedParameterJdbcTemplate jdbc; // sql 을 실행하기 위해 사용되는 객체
	private SimpleJdbcInsert insertAction; // insert 를 편리하게 하기 위한 객체

	private RowMapper<MainPageProductDto> productMapper = BeanPropertyRowMapper.newInstance(MainPageProductDto.class);
	private RowMapper<DetailProductDto> detailMapper = BeanPropertyRowMapper.newInstance(DetailProductDto.class);
	private RowMapper<PreviewCommentDto> commentMapper = BeanPropertyRowMapper.newInstance(PreviewCommentDto.class);
	private RowMapper<CommentTotalValueDto> commentTotalMapper = BeanPropertyRowMapper.newInstance(CommentTotalValueDto.class);
	private RowMapper<ImageDto> filesMapper = BeanPropertyRowMapper.newInstance(ImageDto.class);
	private RowMapper<Price> pricesMapper = BeanPropertyRowMapper.newInstance(Price.class);
	

	public ProductDao(DataSource dataSource) {
		this.jdbc = new NamedParameterJdbcTemplate(dataSource);
	}

	public Map<String, Object> selectProductsByCategoryId(Long id, int limit, int offset) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> dataParams = new HashMap<String, Object>();
		Map<String, ?> counterParams = Collections.singletonMap("category_id", id);
		
		dataParams.put("category_id", id);
		dataParams.put("limit", limit);
		dataParams.put("offset", offset);
		
		List<MainPageProductDto> products = jdbc.query(ProductSqls.SELECT_BY_CATEGORY_ID, dataParams, productMapper);
		Long totalCount = jdbc.queryForObject(ProductSqls.SELECT_TOTAL_COUNT_BY_CATEGORY_ID, counterParams, Long.class);
		
		result.put("totalCount", totalCount);
		result.put("products", products);
		
		if(products == null || products.isEmpty() || totalCount == 0) {
			return null;
		}
		
		return result;
	}
	
	public Map<String, Object> selectAllProduct(int limit, int offset) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> dataParams = new HashMap<String, Object>();
		Map<String, Object> emptyParams = new HashMap<String, Object>();
		
		dataParams.put("limit", limit);
		dataParams.put("offset", offset);

		List<MainPageProductDto> products = jdbc.query(ProductSqls.SELECT_ALL, dataParams, productMapper);
		Long totalCount = jdbc.queryForObject(ProductSqls.SELECT_TOTAL_COUNT, emptyParams , Long.class);
		
		result.put("totalCount", totalCount);
		result.put("products", products);
		
		if(products == null || products.isEmpty() || totalCount == 0) {
//			throw new EmptyResultDataAccessException(1);
			return null;
		}
		
		return result;
	}
	
	public Map<String, Object> selectProductById(long id) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, ?> params = Collections.singletonMap("pid", id);
		
		DetailProductDto detailProduct = jdbc.queryForObject(ProductSqls.SELECT_DETAIL_PRODUCT_BY_ID, params, detailMapper);
		List<ImageDto> files = jdbc.query(ProductSqls.DETAIL_PRODUCT_IMGAE_LIST, params, filesMapper);
			
		detailProduct.setFiles(files);
		
		if(detailProduct == null || files == null || files.isEmpty() || files.size() == 0) return null;
	
		result.put("product", detailProduct);
		
		return result;
	}
	
	public Map<String, Object> selectProductPreviewComments(long id, int limit, int offset) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> imageParam = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pid", id);
		params.put("limit", limit);
		params.put("offset", offset);
		
		List<PreviewCommentDto> previewComments = jdbc.query(ProductSqls.PREVIEW_COMMENTS_BY_PRODUCT_ID, params, commentMapper);
		CommentTotalValueDto previewCommentTotalValue = jdbc.queryForObject(ProductSqls.PREVIEW_COMMENTS_TOTAL_VALUES, params, commentTotalMapper);
		
		for(PreviewCommentDto previewComment: previewComments) {
			imageParam = new HashMap<String, Object>();
			imageParam.put("pid", id);
			imageParam.put("cid", previewComment.getCid());
			
			List<ImageDto> commentImages = jdbc.query(ProductSqls.PREVIEW_COMMENTS_IMAGE_LIST, imageParam, filesMapper);
			previewComment.setImages(commentImages);
		}
		
		result.put("total", previewCommentTotalValue);
		result.put("comments", previewComments);

		return result;
	}
	
	public Map<String, Object> selectReservableProductById(long id) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, ?> params = Collections.singletonMap("pid", id);
		
		DetailProductDto detailProduct = jdbc.queryForObject(ProductSqls.SELECT_DETAIL_PRODUCT_BY_ID, params, detailMapper);
		List<ImageDto> files = jdbc.query(ProductSqls.DETAIL_PRODUCT_IMGAE_LIST, params, filesMapper);
		List<Price> prices = jdbc.query(ProductSqls.DETAIL_PRODUCT_PRICE, params, pricesMapper);
			
		detailProduct.setFiles(files);
		detailProduct.setPrices(prices);
		
		if(detailProduct == null || files == null || files.isEmpty() || files.size() == 0) return null;
		if(prices == null || prices.size() == 0) return null;
	
		result.put("product", detailProduct);
		
		return result;
	}

}
