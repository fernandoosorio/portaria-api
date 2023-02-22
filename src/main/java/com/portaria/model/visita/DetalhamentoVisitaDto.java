package com.portaria.model.visita;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DetalhamentoVisitaDto {
	private Long id;
	private String nomePessoa;
	private LocalDateTime horarioEntrada;
	private LocalDateTime horarioSaida;	   
    private String nomeCadastrador;
    private boolean ativo;
	
	
	public DetalhamentoVisitaDto(Visita entidade) {
		this.id = entidade.getId();
		this.nomePessoa = entidade.getPessoa().getNome();
		this.horarioEntrada = entidade.getDataEntrada();
		this.horarioSaida = entidade.getDataSaida();
		this.ativo = entidade.isAtivo();
		//this.nomeCadastrador = pessoa.getUsuarioCadastrador().getNome();
	}

}
