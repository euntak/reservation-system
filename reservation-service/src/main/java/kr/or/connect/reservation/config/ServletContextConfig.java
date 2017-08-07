package kr.or.connect.reservation.config;

import kr.or.connect.reservation.interceptor.LoggingHandlerInterceptor;
import kr.or.connect.reservation.resolver.CommentWebArgumentResolver;
import kr.or.connect.reservation.interceptor.LoginInterceptor;
import kr.or.connect.reservation.resolver.UserWebArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "kr.or.connect.reservation.controller" ,"kr.or.connect.reservation.interceptor"})
public class ServletContextConfig extends WebMvcConfigurerAdapter {

	@Autowired
	LoginInterceptor loginInterceptor;

	@Autowired
	LoggingHandlerInterceptor loggingHandlerInterceptor;


	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// webapp/resources 경로를 의미
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginInterceptor)
				.addPathPatterns("/my").addPathPatterns("/products/{id}/reserve");
		registry.addInterceptor(loggingHandlerInterceptor)
				.addPathPatterns("/**");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new CommentWebArgumentResolver()); //사용자가 작성한 객체
		argumentResolvers.add(new UserWebArgumentResolver());
	}
}
