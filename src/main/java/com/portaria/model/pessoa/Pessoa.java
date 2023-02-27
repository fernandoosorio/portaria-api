package com.portaria.model.pessoa;

import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.swing.text.MaskFormatter;

import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.infra.dominio.Usuario;
import com.portaria.model.visita.Visita;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pessoa", schema = "portaria")
@Data
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class Pessoa implements Serializable  {
    private static final long serialVersionUID = 1L;

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
    private String telefoneFixo;
    
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
    
    @JsonIgnore
    @OneToMany(mappedBy = "pessoa", fetch = FetchType.EAGER)
    private List<Visita> visitas = new ArrayList<>();
    
    @Column
    private String caminhoFoto;
    
    @Transient
    private Usuario usuario;

    public Pessoa (Long id) {
    	this.id = id;
    }

    public Pessoa(PessoaCadastrarDto dto) {
    	this.nome = dto.getNome();
    	this.cpf = dto.getCpf();
    	this.telefoneCelular = dto.getTelefoneCelular();
    	this.dataCriacao = LocalDateTime.now();
    	this.idUsuarioCadastrador = dto.getIdUsuarioCadastrador();
    	this.ativo = dto.isAtivo();
    	this.caminhoFoto =  dto.getCaminhoFoto();
    	this.telefoneFixo = dto.getTelefoneFixo();
		
	}


	public void atualizarDados(PessoaAtualizaraDto dto) {
		if(!ObjectUtils.isEmpty(dto.getNome())) {
			this.nome = dto.getNome();
		}
		if(!ObjectUtils.isEmpty(dto.getTelefoneCelular())) {
			this.telefoneCelular = dto.getTelefoneCelular();
		}
		if(!ObjectUtils.isEmpty(dto.getIdUsuarioModificador())) {
			this.idUsuarioUltimaAtualizacao = dto.getIdUsuarioModificador() ;
		}
		
		if(!ObjectUtils.isEmpty(dto.getTelefoneFixo())) {
			this.telefoneFixo = dto.getTelefoneFixo();
		}
		if(!ObjectUtils.isEmpty(dto.getAtivo())) {
			this.ativo = dto.getAtivo();
		}
		
		
		
		this.dataUltimaAtualizacao = LocalDateTime.now();
		
	}
	
	public String getCpfFormatado() {
		  if ( cpf == null) return null;
		  try {
		   MaskFormatter mask = new MaskFormatter("###.###.###-##");
		   mask.setValueContainsLiteralCharacters(false);
		   return mask.valueToString(cpf);
		  }
		  catch (ParseException ex) {
		   ex.printStackTrace();
		   return null;
		  }
		 }


}
