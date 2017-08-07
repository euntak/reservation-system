package kr.or.connect.reservation.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.or.connect.reservation.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductRestContoller {

	@Autowired
	ProductService service;
	
	// DEFAULT VALUE
	private static final int LIMIT = 10;
	private static final int OFFSET = 0;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> getProductsList(
			@RequestParam(value = "limit", required = false) String limit,
			@RequestParam(value = "offset", required = false) String offset,
			@RequestParam(value = "cid", required = false) String cid) {

		int limitVal = (limit != null) ? Integer.parseInt(limit) : this.LIMIT;
		int offsetVal = (offset != null) ? Integer.parseInt(offset) : this.OFFSET;
		Long categoryId = (cid != null) ? Long.parseLong(cid) : 0;
		
		if(categoryId != null && categoryId > 0) {
			return service.getProductsByCategoryId(categoryId, limitVal, offsetVal);
		}

		return service.getAllProduct(limitVal, offsetVal);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> getProductById(@PathVariable("id") Long id) {
		return service.getProductById(id);
	}
	
	@GetMapping("/{id}/reserve")
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> selectReservableProductById(@PathVariable("id") Long id) {
		return service.getReservableProduct(id);
	}
	
	@GetMapping("/{id}/comments")
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> getProductPreviewCommentsById(
			@PathVariable("id") Long id,
			@RequestParam(value = "limit", required = false) String limit,
			@RequestParam(value = "offset", required = false) String offset) {

		int limitVal = (limit != null) ? Integer.parseInt(limit) : this.LIMIT;
		int offsetVal = (offset != null) ? Integer.parseInt(offset) : this.OFFSET;

		return service.getProductPreviewComments(id, limitVal, offsetVal);
	}
}
