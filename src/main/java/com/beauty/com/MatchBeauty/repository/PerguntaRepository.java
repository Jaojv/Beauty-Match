package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.Pergunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerguntaRepository extends JpaRepository<Pergunta, Long> {
    
    /**
     * Busca todas as perguntas ativas ordenadas por ordem
     */
    @Query("SELECT p FROM Pergunta p WHERE p.ativo = true ORDER BY p.ordem ASC")
    List<Pergunta> findAllAtivasOrderByOrdem();
    
    /**
     * Busca perguntas ativas por ordem específica
     */
    List<Pergunta> findByAtivoTrueOrderByOrdemAsc();
    
    /**
     * Verifica se existe pergunta com a ordem especificada
     */
    boolean existsByOrdemAndAtivoTrue(Integer ordem);
    
    /**
     * Busca a próxima ordem disponível
     */
    @Query("SELECT COALESCE(MAX(p.ordem), 0) + 1 FROM Pergunta p WHERE p.ativo = true")
    Integer findNextOrdem();
} 