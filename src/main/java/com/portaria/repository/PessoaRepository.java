package com.portaria.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.portaria.model.pessoa.Pessoa;


public interface PessoaRepository  extends JpaRepository<Pessoa, Long>{
	
    @Query("FROM Pessoa p where ativo = true ")
    Page<Pessoa> buscarPaginado( Pageable pageable);


    @Query("SELECT p FROM Pessoa p WHERE 1 = 1 "
    		 +"AND ( :nome is null or lower(nome) LIKE  lower(CONCAT('%',:nome,'%')) ) "
    		 +"AND ( :cpf is null or lower(p.cpf) LIKE  lower(CONCAT('%',:cpf,'%')) ) "
//    +"AND (CAST(:data AS date) is null or DATE(l.data) = DATE(:data) ) "
			+"AND (:ativo is null or ativo = :ativo) " 
    )
    Page<Pessoa> findByParametros(
    		@Param("nome") String nome,
    		@Param("cpf") String cpf,
          //  @Param("data") LocalDateTime data, 
            @Param("ativo") Boolean ativo,
            Pageable pageable);


    List<Pessoa> findByCpf(String cpf);
    
//    @Query("SELECT p FROM Pessoa p "
//    		+ " Left JOIN Usuario u  on p.idUsuarioCadastrador = u.id "
//    		+ " WHERE 1 = 1 "
//   		 +" AND ( :nome is null or lower(p.nome) LIKE  lower(CONCAT('%',:nome,'%')) ) "
//   		 +" AND ( :cpf is null or lower(p.cpf) LIKE  lower(CONCAT('%',:cpf,'%')) ) "
//   +"AND (CAST(:data AS date) is null or DATE(l.data) = DATE(:data) ) "
//			+" AND (:ativo is null or p.ativo = :ativo) " 
//   )
    
//   List<Pessoa> findByParametrosToReport(@Param("nome") String nome, @Param("cpf") String cpf,
//         //  @Param("data") LocalDateTime data, 
//           @Param("ativo") Boolean ativo);
    
}
