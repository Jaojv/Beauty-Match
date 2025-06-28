package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.RespostaQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RespostaQuizRepository extends JpaRepository<RespostaQuiz, Long> {
    
    /**
     * Busca resposta do quiz por cliente
     */
    Optional<RespostaQuiz> findByClienteId(Long clienteId);
    
    /**
     * Verifica se o cliente já respondeu o quiz
     */
    boolean existsByClienteId(Long clienteId);
    
    /**
     * Busca resposta do quiz por cliente com join fetch
     */
    @Query("SELECT rq FROM RespostaQuiz rq WHERE rq.cliente.id = :clienteId")
    Optional<RespostaQuiz> findByClienteIdWithCliente(@Param("clienteId") Long clienteId);
    
    /**
     * Deleta resposta do quiz por cliente (para permitir nova resposta)
     */
    void deleteByClienteId(Long clienteId);
} 