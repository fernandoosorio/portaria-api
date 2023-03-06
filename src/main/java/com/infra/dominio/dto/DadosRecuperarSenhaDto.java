package com.infra.dominio.dto;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class DadosRecuperarSenhaDto {
	@NotBlank private String cpf;
	@NotBlank private String nome;
	@NotBlank private String dataNascimento;
	@NotBlank private String email;
	@NotBlank private String senha;
	
	public LocalDate getDataNacimentoAsDate() throws ParseException {
//		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); 
//		Date dataFormatada = formato.parse(this.dataNascimento);
		LocalDate dataFormatada =  LocalDate.parse(dataNascimento, 
    			DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		return dataFormatada;
	}
	

}
