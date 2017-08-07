package kr.or.connect.reservation.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.mysql.fabric.xmlrpc.base.Param;

public class MainControllerTest {

	private MockMvc mockMvc;

	// 테스트 메소드 실행전 셋업 메소드입니다.
	@Before
	public void setup(){
		// 이곳에서 HomeController를 MockMvc 객체로 만듭니다.
		this.mockMvc = MockMvcBuilders.standaloneSetup(new MainController()).build();
	}

	@Test
	public void test() throws Exception{
		this.mockMvc.perform(get("/"))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	public void myreservation() throws Exception{
		this.mockMvc.perform(get("/my"))
		.andDo(print())
		.andExpect(status().isFound());//302 리다이렉트
	}
	
	@Test
	public void detailProduct() throws Exception{
		this.mockMvc.perform(get("/products/{id}", 1))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("id"));
	}
	
	@Test
	public void reserveProduct() throws Exception{
		this.mockMvc.perform(get("/products/1/reserve"))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	public void review() throws Exception{
		this.mockMvc.perform(get("/review"))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	public void login() throws Exception{
		this.mockMvc.perform(get("/login"))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	public void logout() throws Exception{
		this.mockMvc.perform(get("/logout"))
		.andDo(print())
		.andExpect(status().isFound());//302 리다이렉트
		
	}
}
