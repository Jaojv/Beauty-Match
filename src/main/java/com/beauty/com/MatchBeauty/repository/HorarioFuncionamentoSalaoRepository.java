package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.HorarioFuncionamentoSalao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

/**
 * REPOSITÓRIO HORÁRIO FUNCIONAMENTO SALÃO - ACESSO A DADOS DE HORÁRIOS DE FUNCIONAMENTO
 * 
 * Este repositório fornece métodos para acessar e manipular dados de horários
 * de funcionamento dos salões no sistema Match Beauty. Estende JpaRepository
 * para herdar operações CRUD básicas e adiciona métodos específicos para busca
 * por salão e dia da semana.
 * 
 * FUNCIONALIDADES:
 * - Operações CRUD básicas (herdadas de JpaRepository)
 * - Busca de horários por salão
 * - Busca de horários por salão e dia da semana
 * - Controle de horários ativos
 * - Exclusão de horários por salão
 * 
 * USO:
 * - Consulta de horários de funcionamento
 * - Validação de disponibilidade do salão
 * - Gerenciamento de horários de funcionamento
 *
 */
@Repository
public interface HorarioFuncionamentoSalaoRepository extends JpaRepository<HorarioFuncionamentoSalao, Long> {
    
    /**
     * BUSCA HORÁRIOS DE FUNCIONAMENTO ATIVOS POR SALÃO
     * 
     * @param salaoId ID do salão
     * @return Lista de horários de funcionamento ativos do salão
     */
    List<HorarioFuncionamentoSalao> findBySalaoIdAndAtivoTrue(Long salaoId);
    
    /**
     * BUSCA HORÁRIO DE FUNCIONAMENTO POR SALÃO E DIA DA SEMANA
     * 
     * @param salaoId ID do salão
     * @param diaSemana Dia da semana
     * @return Lista de horários de funcionamento ativos do salão no dia especificado
     */
    List<HorarioFuncionamentoSalao> findBySalaoIdAndDiaSemanaAndAtivoTrue(Long salaoId, DayOfWeek diaSemana);
    
    /**
     * BUSCA TODOS OS HORÁRIOS DE FUNCIONAMENTO POR SALÃO
     * 
     * @param salaoId ID do salão
     * @return Lista de todos os horários de funcionamento do salão (ativos e inativos)
     */
    List<HorarioFuncionamentoSalao> findBySalaoId(Long salaoId);
    
    /**
     * DELETA TODOS OS HORÁRIOS DE FUNCIONAMENTO POR SALÃO
     * 
     * @param salaoId ID do salão
     */
    void deleteBySalaoId(Long salaoId);
} 