package com.infra.dominio.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DadosRecuperarSenhaDto {
	@NotBlank private String cpf;
	@NotBlank private String nome;
	@NotBlank private String dataNascimento;
	@NotBlank private String email;
	@NotBlank private String senha;
	
	public Date getDataNacimentoAsDate() throws ParseException {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); 
		Date dataFormatada = formato.parse(this.dataNascimento);
		
		return dataFormatada;
	}

}
