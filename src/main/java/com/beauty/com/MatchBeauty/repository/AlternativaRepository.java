package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.Alternativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlternativaRepository extends JpaRepository<Alternativa, Long> {
    
    /**
     * Busca todas as alternativas ativas de uma pergunta específica
     */
    @Query("SELECT a FROM Alternativa a WHERE a.pergunta.id = :perguntaId AND a.ativo = true ORDER BY a.id ASC")
    List<Alternativa> findByPerguntaIdAndAtivoTrue(@Param("perguntaId") Long perguntaId);
    
    /**
     * Busca alternativas ativas por pergunta
     */
    List<Alternativa> findByPerguntaIdAndAtivoTrueOrderByIdAsc(Long perguntaId);
    
    /**
     * Verifica se existe alternativa com o texto especificado na pergunta
     */
    boolean existsByTextoAndPerguntaIdAndAtivoTrue(String texto, Long perguntaId);
    
    /**
     * Busca alternativa por texto e pergunta
     */
    Alternativa findByTextoAndPerguntaIdAndAtivoTrue(String texto, Long perguntaId);
} 