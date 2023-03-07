package com.infra.controller;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.text.ParseException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infra.dominio.Usuario;
import com.infra.dominio.UsuarioDto;
import com.infra.dominio.dto.DadosLoginDto;
import com.infra.dominio.dto.DadosRecuperarSenhaDto;
import com.infra.repositorio.UsuarioRepository;
import com.infra.seguranca.TokenService;
import com.infra.utils.CpfUtil;
import com.infra.utils.DatasUtil;

import lombok.var;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UsuarioRepository repositorio;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	
	
	@PostMapping
	public ResponseEntity<UsuarioDto> efetuarLogin(@RequestBody @Validated DadosLoginDto dados) {
		
		var authenticationToken = new UsernamePasswordAuthenticationToken(dados.getLogin(), dados.getSenha());
		
		var authentication =  authenticationManager.authenticate(authenticationToken);
		
		var jwtToken = tokenService.gerarToken( (Usuario)authentication.getPrincipal());
		
		var usuarioDto  = new UsuarioDto((Usuario)authentication.getPrincipal(),jwtToken);
	
		
		return ResponseEntity.ok( usuarioDto);
		
	}
	
	@PostMapping("/recuperar-senha")
	@Transactional
	public ResponseEntity<?> recuperarSenha(@RequestBody @Validated DadosRecuperarSenhaDto dto) throws ParseException {
		
		//Todas as informações obrigatórias foram preenchidas
		List<Usuario> usuarios = repositorio.findByCpfAndNomeIgnoreCaseAndEmailIgnoreCaseAndDataNascimento(
			   CpfUtil.getCpfSomenteNumeros(dto.getCpf()),
				dto.getNome(), 
				dto.getEmail(), 
				DatasUtil.getStringDataToLocalDate(dto.getDataNascimento() ) );
		if(usuarios != null && usuarios.size() > 0 ) {
			usuarios.get(0).setSenha(passwordEncoder.encode(dto.getSenha()));
			return ResponseEntity.ok().build();
			
		}else {
			return ResponseEntity.notFound().build();
		}
		
	}

}
