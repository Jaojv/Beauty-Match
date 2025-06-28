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
    Optional<RespostaQuiz> findByCliente_IdUsuario(Long clienteId);
    
    /**
     * Verifica se o cliente já respondeu o quiz
     */
    boolean existsByCliente_IdUsuario(Long clienteId);
    
    /**
     * Busca resposta do quiz por cliente com join fetch
     */
    @Query("SELECT rq FROM RespostaQuiz rq WHERE rq.cliente.idUsuario = :clienteId")
    Optional<RespostaQuiz> findByClienteIdWithCliente(@Param("clienteId") Long clienteId);
    
    /**
     * Deleta resposta do quiz por cliente (para permitir nova resposta)
     */
    void deleteByCliente_IdUsuario(Long clienteId);
} 