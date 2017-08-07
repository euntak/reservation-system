package kr.or.connect.reservation.controller;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import kr.or.connect.reservation.config.RootApplicationContextConfig;
import kr.or.connect.reservation.dto.MyReservationDto;
import kr.or.connect.reservation.service.ReservationServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootApplicationContextConfig.class)
@WebAppConfiguration
public class ReservationRestControllerTest {

	private MockMvc mvc;

	@Before
	public void setup(){
		this.mvc = MockMvcBuilders.standaloneSetup(new ReservationRestController()).build();
	}

	@Test
	public void contextLoads() throws Exception {
		this.mvc.perform(get("/api/reservation")).andDo(print());
	}

	
	/*private MockMvc mockMvc;

    @Mock
    private ReservationServiceImpl service;

    @InjectMocks
    private ReservationRestController reservationRestController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(reservationRestController)
                .build();
    }
    
    @Test
    public void test_get_all_success() throws Exception {
        Long userId = 1L;
    	List<MyReservationDto> users = Arrays.asList(
                new MyReservationDto("홍길동", 1),
                new MyReservationDto("이순신", 2));
        when(service.selectReservation(userId)).thenReturn(users);
        mockMvc.perform(get("/api/reservation"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$[0].reservationType", is(1)))
                .andExpect(jsonPath("$[0].name", is("홍길동")))
                .andExpect(jsonPath("$[1].reservationType", is(2)))
                .andExpect(jsonPath("$[1].name", is("이순신")));
        verify(service, times(1)).selectReservation(userId);
        verifyNoMoreInteractions(service);
    }*/
}
