package com.portaria.model.pessoa;

import java.time.LocalDateTime;

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

    
}
