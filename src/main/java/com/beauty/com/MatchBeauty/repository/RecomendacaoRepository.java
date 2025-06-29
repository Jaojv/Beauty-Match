package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.Recomendacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecomendacaoRepository extends JpaRepository<Recomendacao, Long> {
    
    /**
     * Busca recomendação por critério exato
     */
    Optional<Recomendacao> findByCriterioAndAtivoTrue(String criterio);
    
    /**
     * Busca todas as recomendações ativas
     */
    @Query("SELECT r FROM Recomendacao r WHERE r.ativo = true ORDER BY r.criterio ASC")
    List<Recomendacao> findAllAtivasOrderByCriterio();
    
    /**
     * Verifica se existe recomendação com o critério especificado
     */
    boolean existsByCriterioAndAtivoTrue(String criterio);
    
    /**
     * Busca recomendações que contenham o critério (para busca parcial)
     */
    @Query("SELECT r FROM Recomendacao r WHERE r.criterio LIKE %:criterio% AND r.ativo = true ORDER BY r.criterio ASC")
    List<Recomendacao> findByCriterioContainingAndAtivoTrue(@Param("criterio") String criterio);
} 