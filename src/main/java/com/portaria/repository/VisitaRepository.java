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


    @Query("SELECT p FROM Pessoa p WHERE 1 = 1 "
    +"AND ( :nome is null or lower(nome) LIKE  lower(CONCAT('%',:nome,'%')) ) "
  //  +"AND (CAST(:data AS date) is null or DATE(l.data) = DATE(:data) ) "
    +"AND (:ativo is null or ativo = :ativo) " 
    )  
    Page<Visita> findByParametros(
    		@Param("nome") String nome,
          //  @Param("data") LocalDateTime data, 
            @Param("ativo") Boolean ativo,
            Pageable pageable);
    
}
