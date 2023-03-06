package com.infra.repositorio;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.infra.dominio.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	UserDetails findByCpf(String cpf);
	
	
	List<Usuario> findByCpfAndNomeAndEmailAndDataNascimento(
			String cpf, String nome, String email, Date dataNascimento);

}
