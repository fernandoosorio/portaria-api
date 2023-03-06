package com.infra.repositorio;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.infra.dominio.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	UserDetails findByCpf(String cpf);
	
	
	List<Usuario> findByCpfAndNomeIgnoreCaseAndEmailIgnoreCaseAndDataNascimento(
			String cpf, String nome, String email, LocalDate dataNascimento);

}
