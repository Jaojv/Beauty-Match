package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * REPOSITÓRIO PROFISSIONAL - ACESSO A DADOS DE PROFISSIONAIS
 * 
 * Este repositório fornece métodos para acessar e manipular dados de profissionais
 * no sistema Match Beauty. Estende JpaRepository para herdar operações CRUD básicas
 * e adiciona métodos específicos para busca por salão.
 * 
 * FUNCIONALIDADES:
 * - Operações CRUD básicas (herdadas de JpaRepository)
 * - Busca de profissionais por salão
 * - Gerenciamento de profissionais
 * 
 * USO:
 * - Consulta de profissionais por salão
 * - Gerenciamento de equipe de salão
 * - Validação de disponibilidade de profissionais
 *
 */
@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    
    /**
     * BUSCA PROFISSIONAIS POR SALÃO
     * 
     * @param salaoId ID do salão
     * @return Lista de profissionais do salão
     */
    List<Profissional> findBySalao_Id(Long salaoId);

    /**
     * VERIFICA SE EXISTE PROFISSIONAL COM USERNAME
     * 
     * @param username Nome de usuário
     * @return true se existe, false caso contrário
     */
    boolean existsByUsername(String username);
} 