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
    private String caminhoFoto;
	
	
	public DetalhamentoPessoaDto(Pessoa entidade) {
		this.id = entidade.getId();
		this.nome = entidade.getNome();
		this.cpf = entidade.getCpf();
		this.dataCriacao = entidade.getDataCriacao();
		this.ativo = entidade.isAtivo();
		this.caminhoFoto = entidade.getCaminhoFoto();
		
		//this.nomeCadastrador = pessoa.getUsuarioCadastrador().getNome();
	}

}
