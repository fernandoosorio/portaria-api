package com.infra.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ExceptionDto {
	
	HttpStatus  StatusCode;
	String mensagem;
	public ExceptionDto(HttpStatus statusCode, String mensagem) {
		StatusCode = statusCode;
		this.mensagem = mensagem;
	}
	
	

}
