package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.Agendamento;
import com.beauty.com.MatchBeauty.entity.Agendamento.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REPOSITÓRIO AGENDAMENTO - ACESSO A DADOS DE AGENDAMENTOS
 * 
 * Este repositório fornece métodos para acessar e manipular dados de agendamentos
 * no sistema Match Beauty. Estende JpaRepository para herdar operações CRUD básicas
 * e adiciona métodos específicos para busca por cliente, profissional, salão,
 * status e período de tempo.
 * 
 * FUNCIONALIDADES:
 * - Operações CRUD básicas (herdadas de JpaRepository)
 * - Busca de agendamentos por cliente
 * - Busca de agendamentos por profissional
 * - Busca de agendamentos por salão
 * - Busca de agendamentos por status
 * - Busca de agendamentos por período de tempo
 * - Combinações de filtros (cliente + período, profissional + período, etc.)
 * 
 * USO:
 * - Consulta de agendamentos de clientes
 * - Consulta de agenda de profissionais
 * - Relatórios de salão
 * - Validação de disponibilidade
 *
 */
@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    
    /**
     * BUSCA AGENDAMENTOS POR CLIENTE
     * 
     * @param clienteId ID do cliente
     * @return Lista de agendamentos do cliente
     */
    List<Agendamento> findByClienteIdUsuario(Long clienteId);
    
    /**
     * BUSCA AGENDAMENTOS POR PROFISSIONAL
     * 
     * @param profissionalId ID do profissional
     * @return Lista de agendamentos do profissional
     */
    List<Agendamento> findByProfissionalIdUsuario(Long profissionalId);
    
    /**
     * BUSCA AGENDAMENTOS POR SALÃO
     * 
     * @param salaoId ID do salão
     * @return Lista de agendamentos do salão
     */
    List<Agendamento> findBySalaoId(Long salaoId);
    
    /**
     * BUSCA AGENDAMENTOS POR STATUS
     * 
     * @param status Status dos agendamentos (AGENDADO, CONCLUIDO, CANCELADO, FALTANTE)
     * @return Lista de agendamentos com o status especificado
     */
    List<Agendamento> findByStatus(StatusAgendamento status);
    
    /**
     * BUSCA AGENDAMENTOS DE CLIENTE POR PERÍODO
     * 
     * @param clienteId ID do cliente
     * @param inicio Data/hora de início do período
     * @param fim Data/hora de fim do período
     * @return Lista de agendamentos do cliente no período especificado
     */
    List<Agendamento> findByClienteIdUsuarioAndDataHoraBetween(
        Long clienteId, 
        LocalDateTime inicio, 
        LocalDateTime fim
    );
    
    /**
     * BUSCA AGENDAMENTOS DE PROFISSIONAL POR PERÍODO
     * 
     * @param profissionalId ID do profissional
     * @param inicio Data/hora de início do período
     * @param fim Data/hora de fim do período
     * @return Lista de agendamentos do profissional no período especificado
     */
    List<Agendamento> findByProfissionalIdUsuarioAndDataHoraBetween(
        Long profissionalId, 
        LocalDateTime inicio, 
        LocalDateTime fim
    );
    
    /**
     * BUSCA AGENDAMENTOS DE SALÃO POR PERÍODO
     * 
     * @param salaoId ID do salão
     * @param inicio Data/hora de início do período
     * @param fim Data/hora de fim do período
     * @return Lista de agendamentos do salão no período especificado
     */
    List<Agendamento> findBySalaoIdAndDataHoraBetween(
        Long salaoId, 
        LocalDateTime inicio, 
        LocalDateTime fim
    );

    /**
     * BUSCA AGENDAMENTOS DE CLIENTE POR STATUS E PERÍODO
     * 
     * @param clienteId ID do cliente
     * @param status Status dos agendamentos
     * @param inicio Data/hora de início do período
     * @param fim Data/hora de fim do período
     * @return Lista de agendamentos do cliente com status específico no período
     */
    List<Agendamento> findByClienteIdUsuarioAndStatusAndDataHoraBetween(
        Long clienteId,
        StatusAgendamento status,
        LocalDateTime inicio,
        LocalDateTime fim
    );

    /**
     * BUSCA AGENDAMENTOS DE PROFISSIONAL POR STATUS E PERÍODO
     * 
     * @param profissionalId ID do profissional
     * @param status Status dos agendamentos
     * @param inicio Data/hora de início do período
     * @param fim Data/hora de fim do período
     * @return Lista de agendamentos do profissional com status específico no período
     */
    List<Agendamento> findByProfissionalIdUsuarioAndStatusAndDataHoraBetween(
        Long profissionalId,
        StatusAgendamento status,
        LocalDateTime inicio,
        LocalDateTime fim
    );

    /**
     * BUSCA AGENDAMENTOS DE SALÃO POR STATUS E PERÍODO
     * 
     * @param salaoId ID do salão
     * @param status Status dos agendamentos
     * @param inicio Data/hora de início do período
     * @param fim Data/hora de fim do período
     * @return Lista de agendamentos do salão com status específico no período
     */
    List<Agendamento> findBySalaoIdAndStatusAndDataHoraBetween(
        Long salaoId,
        StatusAgendamento status,
        LocalDateTime inicio,
        LocalDateTime fim
    );
} 