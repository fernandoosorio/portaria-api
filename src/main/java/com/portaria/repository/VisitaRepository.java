package com.portaria.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.portaria.model.visita.Visita;


public interface VisitaRepository  extends JpaRepository<Visita, Long>{
	
    @Query("FROM Visita v where ativo = true ")
    Page<Visita> buscarPaginado( Pageable pageable);


    @Query("SELECT v FROM Visita v WHERE 1 = 1 "
    +"AND ( :nome is null or lower(v.pessoa.nome) LIKE  lower(CONCAT('%',:nome,'%')) ) "
    +"AND ( :cpf is null or lower(v.pessoa.cpf) LIKE  lower(CONCAT('%',:cpf,'%')) ) "
  //  +"AND (CAST(:data AS date) is null or DATE(l.data) = DATE(:data) ) "
    +"AND (:ativo is null or ativo = :ativo) " 
    )  
    Page<Visita> findByParametros(
    		@Param("nome") String nome,
    		@Param("cpf") String cpf,
          //  @Param("data") LocalDateTime data, 
            @Param("ativo") Boolean ativo,
            Pageable pageable);
    
}
