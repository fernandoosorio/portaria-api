package com.infra.dominio.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class DadosRecuperarSenhaDto {
	@NotBlank private String cpf;
	@NotBlank private String nome;
	@NotBlank private String dataNascimento;
	@NotBlank private String email;
	@NotBlank private String senha;
	
}
