package com.infra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infra.dominio.Usuario;
import com.infra.dominio.UsuarioDto;
import com.infra.dominio.dto.DadosLoginDto;
import com.infra.seguranca.TokenService;

import lombok.var;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping
	public ResponseEntity<UsuarioDto> efetuarLogin(@RequestBody @Validated DadosLoginDto dados) {
		
		var authenticationToken = new UsernamePasswordAuthenticationToken(dados.getLogin(), dados.getSenha());
		
		var authentication =  authenticationManager.authenticate(authenticationToken);
		
		var jwtToken = tokenService.gerarToken( (Usuario)authentication.getPrincipal());
		
		var usuarioDto  = new UsuarioDto((Usuario)authentication.getPrincipal(),jwtToken);
	
		
		return ResponseEntity.ok( usuarioDto);
		
	}

}
