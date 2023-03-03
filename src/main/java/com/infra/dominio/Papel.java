package com.infra.dominio;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "papel", schema = "public")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Papel {

	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "public.SEQ_PAPEL_ID")
	@SequenceGenerator(name = "public.SEQ_PAPEL_ID", sequenceName = "public.SEQ_PAPEL_ID", allocationSize = 1)
	private Long id;
	
	@Column
	private String papel;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="sistema_id")
	private Sistema sistema;
	
	@ManyToMany(mappedBy="papeis")
	private List<Usuario> ususarios;
	
	
	public Papel(String authority) {
		papel = authority;
	}
	
	@Override
	public String toString() {
		return this.papel + " no sistema " + this.sistema.getSistema();
	}

}
