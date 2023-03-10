package com.portaria.model.visita;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DetalhamentoVisitaDto {
	private Long id;
	private String nomePessoa;
	private String cpfPessoa;
	private LocalDateTime horarioEntrada;
	private LocalDateTime horarioSaida;	   
    private String nomeCadastrador;
    private boolean ativo;
    private String codigoVisita;
	
	
	public DetalhamentoVisitaDto(Visita entidade) {
		this.id = entidade.getId();
		this.nomePessoa = entidade.getPessoa().getNome();
		this.horarioEntrada = entidade.getDataEntrada();
		this.horarioSaida = entidade.getDataSaida();
		this.ativo = entidade.isAtivo();
		this.cpfPessoa = entidade.getPessoa().getCpf();
		this.codigoVisita = entidade.getCodigoVisita();
		//this.nomeCadastrador = pessoa.getUsuarioCadastrador().getNome();
	}

}
