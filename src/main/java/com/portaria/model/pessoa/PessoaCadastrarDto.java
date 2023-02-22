package com.portaria.model.pessoa;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class PessoaCadastrarDto {
	@NotBlank
    private String nome;
    private String cpf;
    private String telefoneCelular;
    private boolean ativo = true;
    private Long idUsuarioCadastrador;
    private String caminhoFoto;
    private String telefoneFixo;

    
}
