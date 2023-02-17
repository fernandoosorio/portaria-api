package com.portaria.model.pessoa;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DetalhamentoPessoaDto {
	private Long id;
	private String nome;
	private String cpf;
	private LocalDateTime dataCriacao;	   
    private String nomeCadastrador;
    private boolean ativo;
	
	
	public DetalhamentoPessoaDto(Pessoa pessoa) {
		this.id = pessoa.getId();
		this.nome = pessoa.getNome();
		this.cpf = pessoa.getCpf();
		this.dataCriacao = pessoa.getDataCriacao();
		this.ativo = pessoa.isAtivo();
		//this.nomeCadastrador = pessoa.getUsuarioCadastrador().getNome();
	}

}
