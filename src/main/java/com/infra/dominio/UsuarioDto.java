package com.infra.dominio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.Data;

@Data
public class UsuarioDto {
		
	private Long id;
	private String matricula;
	private String cpf;
	private String token;
	private String nome;
	private List<Papel> papeis = new ArrayList<>();
	private Long idSetorLotacao;
	private String setorLotacao;
		
	@Override
	public String toString() {
		return "Usuario [matricula=" + matricula + ", cpf=" + cpf + "]";
	}


	public UsuarioDto(Usuario principal, String jwtToken) {
		this.token = jwtToken;
		this.cpf = principal.getCpf();
		this.nome = principal.getNome();
		this.id = principal.getId();
		this.matricula = principal.getMatricula();
		this.papeis.addAll( getPapeisNoSistema( principal.getPapeis() ));
		if(principal.getSetorLotacao() != null) {
			this.idSetorLotacao = principal.getSetorLotacao().getId();
			this.setorLotacao = principal.getSetorLotacao().getSetor();
		}
		
	}


	private Collection<? extends Papel> getPapeisNoSistema(List<Papel> papeis2) {
		List<Papel> retorno = new ArrayList<>();
		for(Papel p : papeis2) {
			if(p.getSistema().getSistema().toLowerCase().equals( ("ETICKET").toLowerCase() )) {
				Papel papel = new Papel();
				papel.setId(p.getId());
				papel.setPapel(p.getPapel());
							
				retorno.add(papel);
			}
			
		}
		return retorno;
	}
	
	
	
	

}
