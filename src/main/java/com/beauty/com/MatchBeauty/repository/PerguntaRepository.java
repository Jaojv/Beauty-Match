package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.Pergunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * REPOSITÓRIO PERGUNTA - ACESSO A DADOS DE PERGUNTAS DO QUIZ
 * 
 * Este repositório fornece métodos para acessar e manipular dados de perguntas
 * do quiz de personalização no sistema Match Beauty. Estende JpaRepository para
 * herdar operações CRUD básicas e adiciona métodos específicos para gerenciamento
 * de perguntas ativas e ordem de exibição.
 * 
 * FUNCIONALIDADES:
 * - Operações CRUD básicas (herdadas de JpaRepository)
 * - Busca de perguntas ativas ordenadas
 * - Controle de ordem de exibição
 * - Validação de ordem de perguntas
 * - Cálculo da próxima ordem disponível
 * 
 * USO:
 * - Gerenciamento do quiz de personalização
 * - Controle de sequência de perguntas
 * - Validação de ordem de exibição
 *
 */
@Repository
public interface PerguntaRepository extends JpaRepository<Pergunta, Long> {
    
    /**
     * BUSCA TODAS AS PERGUNTAS ATIVAS ORDENADAS POR ORDEM
     * 
     * @return Lista de perguntas ativas ordenadas por ordem de exibição
     */
    @Query("SELECT p FROM Pergunta p WHERE p.ativo = true ORDER BY p.ordem ASC")
    List<Pergunta> findAllAtivasOrderByOrdem();
    
    /**
     * BUSCA PERGUNTAS ATIVAS POR ORDEM ESPECÍFICA
     * 
     * @return Lista de perguntas ativas ordenadas por ordem de exibição
     */
    List<Pergunta> findByAtivoTrueOrderByOrdemAsc();
    
    /**
     * VERIFICA SE EXISTE PERGUNTA COM A ORDEM ESPECIFICADA
     * 
     * @param ordem Ordem da pergunta
     * @return true se existe pergunta ativa com a ordem, false caso contrário
     */
    boolean existsByOrdemAndAtivoTrue(Integer ordem);
    
    /**
     * BUSCA A PRÓXIMA ORDEM DISPONÍVEL
     * 
     * @return Próxima ordem disponível para nova pergunta
     */
    @Query("SELECT COALESCE(MAX(p.ordem), 0) + 1 FROM Pergunta p WHERE p.ativo = true")
    Integer findNextOrdem();
} 