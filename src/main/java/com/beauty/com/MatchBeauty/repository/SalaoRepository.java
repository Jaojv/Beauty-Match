package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.Salao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * REPOSITÓRIO SALÃO - ACESSO A DADOS DE SALÕES
 * 
 * Este repositório fornece métodos para acessar e manipular dados de salões
 * no sistema Match Beauty. Estende JpaRepository para herdar operações CRUD básicas
 * e adiciona métodos específicos para busca por proprietário e validação de unicidade.
 * 
 * FUNCIONALIDADES:
 * - Operações CRUD básicas (herdadas de JpaRepository)
 * - Busca de salões por proprietário
 * - Busca de salão por nome e endereço
 * - Validação de existência por nome e endereço
 * 
 * USO:
 * - Gerenciamento de salões por proprietários
 * - Validação de unicidade de salões
 * - Consulta de salões disponíveis
 *
 */
@Repository
public interface SalaoRepository extends JpaRepository<Salao, Long> {
    
    /**
     * BUSCA SALÕES POR PROPRIETÁRIO
     * 
     * @param proprietarioId ID do proprietário
     * @return Lista de salões do proprietário
     */
    List<Salao> findByProprietarioIdUsuario(Long proprietarioId);
    
    /**
     * BUSCA SALÃO POR NOME E ENDEREÇO
     * 
     * @param nome Nome do salão
     * @param endereco Endereço do salão
     * @return Optional contendo o salão encontrado ou vazio se não encontrado
     */
    Optional<Salao> findByNomeAndEndereco(String nome, String endereco);
    
    /**
     * VERIFICA SE EXISTE SALÃO COM NOME E ENDEREÇO
     * 
     * @param nome Nome do salão
     * @param endereco Endereço do salão
     * @return true se existe, false caso contrário
     */
    boolean existsByNomeAndEndereco(String nome, String endereco);
} 