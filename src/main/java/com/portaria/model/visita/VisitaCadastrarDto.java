package com.portaria.model.visita;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VisitaCadastrarDto {
	    
	@NotNull
    private Long pessoaId;
	
    private boolean ativo = true;
   
    private Long idUsuario;
        
    private String horaEntrada;
    
    private String horaSaida;
    
    private String destino;

    
}
