package kr.or.connect.reservation.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.Charset;

import javax.net.ssl.SSLEngineResult.Status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.connect.reservation.config.RootApplicationContextConfig;
import kr.or.connect.reservation.service.ProductService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootApplicationContextConfig.class)
@WebAppConfiguration
public class ProductRestContollerTest {

	/*@Mock
    ProductService productService;*/
	
	@InjectMocks
    private ProductRestContoller productRestContoller;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    
    ObjectMapper mapper = new ObjectMapper();
    
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("UTF-8"));

    @Before
    public void setUp() {
         MockitoAnnotations.initMocks(this);
         mockMvc = MockMvcBuilders.standaloneSetup(productRestContoller).build();//autowired
    }
    
    @Test
    public void testCreate() throws Exception {
        this.mockMvc.perform(get("/api/products")
                .contentType(contentType))
                //.content(mapper.writeValueAsString(new MyReservationDto())))
                //.andExpect(status().isNotFound())
                .andDo(print());
 
    }
}
