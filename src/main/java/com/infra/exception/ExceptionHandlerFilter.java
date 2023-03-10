package com.infra.exception;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class ExceptionHandlerFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		try {
            chain.doFilter(request, response);
        } catch (RuntimeException e) {

            // custom error response class used across my project
         ExceptionDto dto = new ExceptionDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        	
        	resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        	resp.getWriter().write( convertObjectToJson (dto));
        }
		
	}
	
	private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

}
