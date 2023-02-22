package com.infra.seguranca;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.infra.dominio.Usuario;

import lombok.var;

@Service
public class TokenService {
	
	@Value("${api.security.token.secret}")
	private String secret;
	
	public String gerarToken(Usuario usuario) {
		
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			
			return JWT.create()
					.withIssuer("Guardiao API")
					.withExpiresAt( dataExpiracao())
					.withSubject(usuario.getCpf())
					.withClaim("matricula", usuario.getMatricula())
					.sign(algorithm);
		}catch (JWTCreationException e) {
			throw new RuntimeException("Erro ao gerar token JWT", e);
		}
		
	}

	private Instant dataExpiracao() {
//		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
		return LocalDateTime.now().plusMinutes(60).toInstant(ZoneOffset.of("-03:00"));
	}
	
	
	public String getSubject(String tokenJWT) {
        try {
                var algoritmo = Algorithm.HMAC256(secret);
                return JWT.require(algoritmo)
                				.withIssuer("Guardiao API")
                                .build()
                                .verify(tokenJWT)
                                .getSubject();
        } catch (JWTVerificationException exception) {
                throw new RuntimeException("Token JWT inválido ou expirado!", exception);
        }
        
	}
	
	public Date getDataExpiracao(String tokenJWT) {
        try {
                var algoritmo = Algorithm.HMAC256(secret);
                return JWT.require(algoritmo)
                				.withIssuer("Guardiao API")
                                .build()
                                .verify(tokenJWT)
                                .getExpiresAt();
        } catch (JWTVerificationException exception) {
                throw new RuntimeException("Token JWT inválido ou expirado!", exception);
        }
        
	}


}
