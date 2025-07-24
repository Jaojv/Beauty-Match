package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * REPOSITÓRIO SERVIÇO - ACESSO A DADOS DE SERVIÇOS
 * 
 * Este repositório fornece métodos para acessar e manipular dados de serviços
 * no sistema Match Beauty. Estende JpaRepository para herdar operações CRUD básicas
 * e adiciona métodos específicos para busca por salão e validação de unicidade.
 * 
 * FUNCIONALIDADES:
 * - Operações CRUD básicas (herdadas de JpaRepository)
 * - Busca de serviços por salão
 * - Busca de serviço por nome e salão
 * - Validação de existência por nome e salão
 * 
 * USO:
 * - Gerenciamento de serviços por salão
 * - Validação de unicidade de serviços
 * - Consulta de serviços disponíveis
 *
 */
@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    
    /**
     * BUSCA SERVIÇOS POR SALÃO
     * 
     * @param salaoId ID do salão
     * @return Lista de serviços do salão
     */
    List<Servico> findBySalaoId(Long salaoId);
    
    /**
     * BUSCA SERVIÇO POR NOME E SALÃO
     * 
     * @param nome Nome do serviço
     * @param salaoId ID do salão
     * @return Optional contendo o serviço encontrado ou vazio se não encontrado
     */
    Optional<Servico> findByNomeAndSalaoId(String nome, Long salaoId);
    
    /**
     * VERIFICA SE EXISTE SERVIÇO COM NOME E SALÃO
     * 
     * @param nome Nome do serviço
     * @param salaoId ID do salão
     * @return true se existe, false caso contrário
     */
    boolean existsByNomeAndSalaoId(String nome, Long salaoId);
} 