package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.Alternativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * REPOSITÓRIO ALTERNATIVA - ACESSO A DADOS DE ALTERNATIVAS DO QUIZ
 * 
 * Este repositório fornece métodos para acessar e manipular dados de alternativas
 * do quiz de personalização no sistema Match Beauty. Estende JpaRepository para
 * herdar operações CRUD básicas e adiciona métodos específicos para gerenciamento
 * de alternativas por pergunta.
 * 
 * FUNCIONALIDADES:
 * - Operações CRUD básicas (herdadas de JpaRepository)
 * - Busca de alternativas por pergunta
 * - Controle de alternativas ativas
 * - Validação de unicidade de texto por pergunta
 * - Ordenação de alternativas
 * 
 * USO:
 * - Gerenciamento de alternativas do quiz
 * - Validação de alternativas únicas
 * - Controle de alternativas ativas
 *
 */
@Repository
public interface AlternativaRepository extends JpaRepository<Alternativa, Long> {
    
    /**
     * BUSCA TODAS AS ALTERNATIVAS ATIVAS DE UMA PERGUNTA ESPECÍFICA
     * 
     * @param perguntaId ID da pergunta
     * @return Lista de alternativas ativas da pergunta ordenadas por ID
     */
    @Query("SELECT a FROM Alternativa a WHERE a.pergunta.id = :perguntaId AND a.ativo = true ORDER BY a.id ASC")
    List<Alternativa> findByPerguntaIdAndAtivoTrue(@Param("perguntaId") Long perguntaId);
    
    /**
     * BUSCA ALTERNATIVAS ATIVAS POR PERGUNTA
     * 
     * @param perguntaId ID da pergunta
     * @return Lista de alternativas ativas da pergunta ordenadas por ID
     */
    List<Alternativa> findByPerguntaIdAndAtivoTrueOrderByIdAsc(Long perguntaId);
    
    /**
     * VERIFICA SE EXISTE ALTERNATIVA COM O TEXTO ESPECIFICADO NA PERGUNTA
     * 
     * @param texto Texto da alternativa
     * @param perguntaId ID da pergunta
     * @return true se existe alternativa ativa com o texto na pergunta, false caso contrário
     */
    boolean existsByTextoAndPerguntaIdAndAtivoTrue(String texto, Long perguntaId);
    
    /**
     * BUSCA ALTERNATIVA POR TEXTO E PERGUNTA
     * 
     * @param texto Texto da alternativa
     * @param perguntaId ID da pergunta
     * @return Alternativa encontrada ou null se não encontrada
     */
    Alternativa findByTextoAndPerguntaIdAndAtivoTrue(String texto, Long perguntaId);
} 