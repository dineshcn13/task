package com.dinesh.task.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class MultipartResolverConfig {
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
	
	    final CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
	    multipartResolver.setMaxUploadSize(100000);
	
	    return multipartResolver;
	}	

}