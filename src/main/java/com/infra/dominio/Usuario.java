package com.infra.dominio;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario", schema = "public")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario implements UserDetails {
		
	private static final long serialVersionUID = 1L;
	
	@Id
	@EqualsAndHashCode.Include
	private Long id;
	
	@Column
	private String matricula;
	@Column
	private String cpf;
	@Column
	private String senha;
	@Column
	private String nome;
	
	
	@ManyToMany
	@JoinTable(
	  name = "usuario_papel", 
	  joinColumns = @JoinColumn(name = "usuario_id"), 
	  inverseJoinColumns = @JoinColumn(name = "papel_id"))
	private List<Papel> papeis;
	
	@OneToOne
	@JoinColumn(name ="setor_id")
	private Setor setorLotacao;
	
	@Column
	private String sexo;
	@Column
	private String email;
	@Column
	private Date dataNascimento;
	@Column
	private String nomeMae;
	@Column
	private Long tipoFuncionarioId;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList( new SimpleGrantedAuthority("ROLE_teste"));
	}

	@Override
	public String getPassword() {
		return this.senha;
	}

	@Override
	public String getUsername() {
		return this.cpf;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String toString() {
		return "Usuario [matricula=" + matricula + ", cpf=" + cpf + "]";
	}
	
	public Usuario (Long id) {
		this.id = id;
	}
	
	public String getUsuarioLogado() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
		    String currentUserName = authentication.getName();
		    return currentUserName;
		}
		return null;
		
		
	}
	

}
