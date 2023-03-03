package com.portaria.model.visita;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class VisitaAtualizaraDto {
	@NotBlank
	private Long id;

    private String destino;
    private boolean ativo;
    private Long idUsuario;
    private String horarioSaidaRegistrar;
    private boolean registrandoSaida;

    
}
