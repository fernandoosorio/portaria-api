package com.infra.dominio;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sistema", schema = "public")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Sistema  {
			
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "public.SEQ_SISTEMA_ID")
	@SequenceGenerator(name = "public.SEQ_SISTEMA_ID", sequenceName = "public.SEQ_SISTEMA_ID", allocationSize = 1)
	private Long id;
	
	@Column
	@EqualsAndHashCode.Include
	private String sistema;
	@Column
	private String url;
	
	@OneToMany(mappedBy = "sistema")
	private Set<Papel> papeis;
	

}
