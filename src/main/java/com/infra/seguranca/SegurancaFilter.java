package com.infra.seguranca;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.infra.repositorio.UsuarioRepository;

import lombok.var;


@Component
public class SegurancaFilter implements Filter{
	
	@Autowired
	private TokenService service;
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
			throws ServletException, IOException {
		HttpServletRequest req = (HttpServletRequest) request;
		
		
		var tokenRequest = recuperarToken(req);
		if(tokenRequest != null && tokenRequest.length() > 12) {
			var subject = service.getSubject(tokenRequest);
			
			var usuario = usuarioRepository.findByCpf(subject);
			
			var authentication = new UsernamePasswordAuthenticationToken(usuario,usuario.getPassword(), usuario.getAuthorities());
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		
		filterChain.doFilter(request, response);
		
	}
	
	
	private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
        	return authorizationHeader.replace("Bearer ","");
               
        }
        return null;

        
	}


}
