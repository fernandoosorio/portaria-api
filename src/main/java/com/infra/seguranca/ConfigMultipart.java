package com.infra.seguranca;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class ConfigMultipart {
	
	@Bean
	public MultipartResolver multipartResolver() {
	   return new CommonsMultipartResolverMine();
	}


	public static class CommonsMultipartResolverMine extends CommonsMultipartResolver {

	   @Override
	   public boolean isMultipart(HttpServletRequest request) {
	      final String header = request.getHeader("Content-Type");
	      if(header == null){
	         return false;
	      }
	      return header.contains("multipart/form-data");
	   }

	}

}
