package com.infra.dominio;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "setor", schema = "public")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Setor  {
			
	@Id
	@EqualsAndHashCode.Include
	private Long id;
	@Column
	private String setor;
	@Column
	private boolean ativo;
	
	@OneToMany(mappedBy = "setorLotacao")
	private Set<Usuario> usuarios;
	

}
