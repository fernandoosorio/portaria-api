package com.portaria.model.pessoa;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.util.ObjectUtils;

import com.infra.dominio.Usuario;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pessoa", schema = "portaria")
@Data
@NoArgsConstructor
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "portaria.SEQ_PESSOA_ID")
	@SequenceGenerator(name = "portaria.SEQ_PESSOA_ID", sequenceName = "portaria.SEQ_PESSOA_ID", allocationSize = 1)
    private Long id;
    
    @Column
    private String nome;
    
    @Column
    private String cpf;
    
    @Column
    private String telefoneCelular;
    
    @Column
    private LocalDateTime dataCriacao;
    
    @Column
    private Long idUsuarioCadastrador;

    @Column
    private boolean ativo;
    
    @Column
    private LocalDateTime dataUltimaAtualizacao;
    
    @Column
    private Long idUsuarioUltimaAtualizacao;


    public Pessoa(PessoaCadastrarDto pessoa) {
    	this.nome = pessoa.getNome();
    	this.cpf = pessoa.getCpf();
    	this.telefoneCelular = pessoa.getTelefoneCelular();
    	this.dataCriacao = LocalDateTime.now();
    	this.idUsuarioCadastrador = pessoa.getIdUsuarioCadastrador();
    	this.ativo = pessoa.isAtivo();
		
	}


	public void atualizarDados(PessoaAtualizaraDto atualizarDto) {
		if(!ObjectUtils.isEmpty(atualizarDto.getNome())) {
			this.nome = atualizarDto.getNome();
		}
		if(!ObjectUtils.isEmpty(atualizarDto.getTelefoneCelular())) {
			this.telefoneCelular = atualizarDto.getTelefoneCelular();
		}
		if(!ObjectUtils.isEmpty(atualizarDto.getIdUsuarioModificador())) {
			this.idUsuarioUltimaAtualizacao = atualizarDto.getIdUsuarioModificador() ;
		}
		
		this.dataUltimaAtualizacao = LocalDateTime.now();

		
	}


    
   

    
}
