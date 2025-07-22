package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.Recomendacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * REPOSITÓRIO RECOMENDAÇÃO - ACESSO A DADOS DE RECOMENDAÇÕES
 * 
 * Este repositório fornece métodos para acessar e manipular dados de recomendações
 * personalizadas no sistema Match Beauty. Estende JpaRepository para herdar
 * operações CRUD básicas e adiciona métodos específicos para busca por critério
 * e controle de recomendações ativas.
 * 
 * FUNCIONALIDADES:
 * - Operações CRUD básicas (herdadas de JpaRepository)
 * - Busca de recomendação por critério exato
 * - Busca de recomendações ativas ordenadas
 * - Validação de existência por critério
 * - Busca parcial por critério (LIKE)
 * 
 * USO:
 * - Gerenciamento de recomendações personalizadas
 * - Busca de recomendações por critério
 * - Validação de critérios únicos
 *
 */
@Repository
public interface RecomendacaoRepository extends JpaRepository<Recomendacao, Long> {
    
    /**
     * BUSCA RECOMENDAÇÃO POR CRITÉRIO EXATO
     * 
     * @param criterio Critério da recomendação
     * @return Optional contendo a recomendação encontrada ou vazio se não encontrada
     */
    Optional<Recomendacao> findByCriterioAndAtivoTrue(String criterio);
    
    /**
     * BUSCA TODAS AS RECOMENDAÇÕES ATIVAS
     * 
     * @return Lista de recomendações ativas ordenadas por critério
     */
    @Query("SELECT r FROM Recomendacao r WHERE r.ativo = true ORDER BY r.criterio ASC")
    List<Recomendacao> findAllAtivasOrderByCriterio();
    
    /**
     * VERIFICA SE EXISTE RECOMENDAÇÃO COM O CRITÉRIO ESPECIFICADO
     * 
     * @param criterio Critério da recomendação
     * @return true se existe recomendação ativa com o critério, false caso contrário
     */
    boolean existsByCriterioAndAtivoTrue(String criterio);
    
    /**
     * BUSCA RECOMENDAÇÕES QUE CONTENHAM O CRITÉRIO (PARA BUSCA PARCIAL)
     * 
     * @param criterio Critério parcial para busca
     * @return Lista de recomendações que contêm o critério, ordenadas por critério
     */
    @Query("SELECT r FROM Recomendacao r WHERE r.criterio LIKE %:criterio% AND r.ativo = true ORDER BY r.criterio ASC")
    List<Recomendacao> findByCriterioContainingAndAtivoTrue(@Param("criterio") String criterio);
} 