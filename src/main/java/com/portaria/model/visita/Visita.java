package com.portaria.model.visita;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.infra.dominio.Usuario;
import com.portaria.model.pessoa.Pessoa;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "visita", schema = "portaria")
@Data
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class Visita  implements Serializable{
    private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "portaria.SEQ_VISITA_ID")
	@SequenceGenerator(name = "portaria.SEQ_VISITA_ID", sequenceName = "portaria.SEQ_VISITA_ID", allocationSize = 1)
    private Long id;
    
    @Column
    private String destino;
    
    @ManyToOne
    @JoinColumn(name = "pessoaid" )
    private Pessoa pessoa;
    
    @Column
    private LocalDateTime dataEntrada;
    
    @Column
    private LocalDateTime dataSaida;
    
    @Column
    private Long idUsuarioCadastrador;
    
    @Column
    private Long idUsuarioUltimaAtualizacao;
    
    @Column
    private LocalDateTime dataUltimaAtualizacao;

    @Column
    private boolean ativo;
    
    @Column
    private String codigoVisita;
    
    @Transient
    private Usuario usuario;
    
  

    public Visita(VisitaCadastrarDto dto) {
    	this.destino = dto.getDestino();
    	this.pessoa = new Pessoa(dto.getPessoaId());
    	this.idUsuarioCadastrador = dto.getIdUsuario();
    	if(dto.getHoraEntrada() != null) {
    		this.dataEntrada = LocalDateTime.parse(dto.getHoraEntrada(), 
        			DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    	}else {
    		this.dataEntrada = LocalDateTime.now();
    	}
    	this.ativo = dto.isAtivo();
    	if(codigoVisita == null || codigoVisita.isEmpty()) {
    		this.codigoVisita = String.valueOf(LocalDateTime.now().hashCode());
    	}
    	
		
	}


	public void atualizarDados(VisitaAtualizaraDto dto) {
		if(!ObjectUtils.isEmpty(  dto.getDestino() )) {
			this.destino = dto.getDestino();
		}
		
		if(!ObjectUtils.isEmpty(dto.getIdUsuario())) {
			this.idUsuarioUltimaAtualizacao = dto.getIdUsuario() ;
		}
		if(dto.isRegistrandoSaida() ) {
			if(dto.getHorarioSaidaRegistrar() != null ) {
	    		this.dataSaida = LocalDateTime.parse(dto.getHorarioSaidaRegistrar(), 
	        			DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
	    	}else {
	    		this.dataSaida = LocalDateTime.now();
	    	}
		}
		this.dataUltimaAtualizacao = LocalDateTime.now();

		
	}

}
