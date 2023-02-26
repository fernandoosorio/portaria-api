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
    private String telefoneCelular;
    private String telefoneFixo;
	
	
	public DetalhamentoPessoaDto(Pessoa entidade) {
		setId( entidade.getId() );
		setNome( entidade.getNome() );
		setCpf( entidade.getCpf() );
		setDataCriacao( entidade.getDataCriacao() );
		setAtivo ( entidade.isAtivo() );
		setCaminhoFoto (entidade.getCaminhoFoto());
		setTelefoneCelular (entidade.getTelefoneCelular());
		setTelefoneFixo ( entidade.getTelefoneFixo() );
		
		//this.nomeCadastrador = pessoa.getUsuarioCadastrador().getNome();
	}

}
