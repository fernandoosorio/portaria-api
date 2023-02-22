package com.portaria.model.pessoa;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PessoaAtualizaraDto {
	@NotNull
	private Long id;
	private String cpf;
    private String nome;
    private String telefoneCelular;
    private String telefoneFixo;
    private boolean ativo;
    private Long idUsuarioModificador;

    
}
