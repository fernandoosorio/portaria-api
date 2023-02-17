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
@Table(name = "sistema", schema = "public")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Sistema  {
			
	@Id
	private Long id;
	
	@Column
	@EqualsAndHashCode.Include
	private String sistema;
	@Column
	private String url;
	
	@OneToMany(mappedBy = "sistema")
	private Set<Papel> papeis;
	

}
