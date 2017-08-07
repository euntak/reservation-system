package kr.or.connect.reservation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.multipart.MultipartResolver;

@Configuration
@PropertySource("classpath:/files.properties")
public class FilesConfig {

	@Value("${spring.uploadfile.max-size}")
	private long uploadMaxFileSize;

	@Value("${spring.uploadfile.root-directory}")
	private String uploadRootDirectory;

	// this bean needed to resolve ${property.name} syntax
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public MultipartResolver multipartResolver() {
		org.springframework.web.multipart.commons.CommonsMultipartResolver multipartResolver = new org.springframework.web.multipart.commons.CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(uploadMaxFileSize);
		return multipartResolver;
	}
}
