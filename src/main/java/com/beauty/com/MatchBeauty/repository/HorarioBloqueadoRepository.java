package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.HorarioBloqueado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REPOSITÓRIO HORÁRIO BLOQUEADO - ACESSO A DADOS DE HORÁRIOS BLOQUEADOS
 * 
 * Este repositório fornece métodos para acessar e manipular dados de horários
 * bloqueados no sistema Match Beauty. Estende JpaRepository para herdar
 * operações CRUD básicas e adiciona métodos específicos para busca por salão
 * e período de tempo.
 * 
 * FUNCIONALIDADES:
 * - Operações CRUD básicas (herdadas de JpaRepository)
 * - Busca de bloqueios por salão
 * - Busca de bloqueios por salão e período
 * - Controle de horários bloqueados
 * 
 * USO:
 * - Consulta de bloqueios de horários
 * - Validação de disponibilidade
 * - Gerenciamento de bloqueios temporários
 *
 */
@Repository
public interface HorarioBloqueadoRepository extends JpaRepository<HorarioBloqueado, Long> {
    
    /**
     * BUSCA BLOQUEIOS POR SALÃO
     * 
     * @param salaoId ID do salão
     * @return Lista de bloqueios do salão
     */
    List<HorarioBloqueado> findBySalaoId(Long salaoId);
    
    /**
     * BUSCA BLOQUEIOS POR SALÃO E PERÍODO
     * 
     * @param salaoId ID do salão
     * @param inicio Data/hora de início do período
     * @param fim Data/hora de fim do período
     * @return Lista de bloqueios do salão no período especificado
     */
    List<HorarioBloqueado> findBySalaoIdAndDataHoraInicioBetween(Long salaoId, LocalDateTime inicio, LocalDateTime fim);
} 