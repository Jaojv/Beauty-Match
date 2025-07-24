package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.RespostaQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * REPOSITÓRIO RESPOSTA QUIZ - ACESSO A DADOS DE RESPOSTAS DO QUIZ
 * 
 * Este repositório fornece métodos para acessar e manipular dados de respostas
 * do quiz de personalização no sistema Match Beauty. Estende JpaRepository para
 * herdar operações CRUD básicas e adiciona métodos específicos para gerenciamento
 * de respostas por cliente.
 * 
 * FUNCIONALIDADES:
 * - Operações CRUD básicas (herdadas de JpaRepository)
 * - Busca de resposta por cliente
 * - Verificação de existência de resposta
 * - Exclusão de resposta para nova tentativa
 * - Busca otimizada com join fetch
 * 
 * USO:
 * - Gerenciamento de respostas do quiz
 * - Validação de cliente já respondeu
 * - Permissão de nova resposta
 *
 */
@Repository
public interface RespostaQuizRepository extends JpaRepository<RespostaQuiz, Long> {
    
    /**
     * BUSCA RESPOSTA DO QUIZ POR CLIENTE
     * 
     * @param clienteId ID do cliente
     * @return Optional contendo a resposta do quiz ou vazio se não encontrada
     */
    Optional<RespostaQuiz> findByCliente_IdUsuario(Long clienteId);
    
    /**
     * VERIFICA SE O CLIENTE JÁ RESPONDEU O QUIZ
     * 
     * @param clienteId ID do cliente
     * @return true se o cliente já respondeu, false caso contrário
     */
    boolean existsByCliente_IdUsuario(Long clienteId);
    
    /**
     * BUSCA RESPOSTA DO QUIZ POR CLIENTE COM JOIN FETCH
     * 
     * @param clienteId ID do cliente
     * @return Optional contendo a resposta do quiz com dados do cliente ou vazio se não encontrada
     */
    @Query("SELECT rq FROM RespostaQuiz rq WHERE rq.cliente.idUsuario = :clienteId")
    Optional<RespostaQuiz> findByClienteIdWithCliente(@Param("clienteId") Long clienteId);
    
    /**
     * DELETA RESPOSTA DO QUIZ POR CLIENTE (PARA PERMITIR NOVA RESPOSTA)
     * 
     * @param clienteId ID do cliente
     */
    void deleteByCliente_IdUsuario(Long clienteId);
} 