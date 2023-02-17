package com.portaria.model.pessoa;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class PessoaAtualizaraDto {
	@NotBlank
	private Long id;

    private String nome;
    private String telefoneCelular;
    private boolean ativo;
    private Long idUsuarioModificador;

    
}
