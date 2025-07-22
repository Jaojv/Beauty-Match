package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.HorarioTrabalho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

/**
 * REPOSITÓRIO HORÁRIO TRABALHO - ACESSO A DADOS DE HORÁRIOS DE TRABALHO
 * 
 * Este repositório fornece métodos para acessar e manipular dados de horários
 * de trabalho dos profissionais no sistema Match Beauty. Estende JpaRepository
 * para herdar operações CRUD básicas e adiciona métodos específicos para busca
 * por profissional e dia da semana.
 * 
 * FUNCIONALIDADES:
 * - Operações CRUD básicas (herdadas de JpaRepository)
 * - Busca de horários por profissional e dia da semana
 * - Busca de horários ativos por profissional
 * - Controle de horários ativos
 * 
 * USO:
 * - Consulta de disponibilidade de profissionais
 * - Validação de horários de trabalho
 * - Gerenciamento de agenda de profissionais
 *
 */
@Repository
public interface HorarioTrabalhoRepository extends JpaRepository<HorarioTrabalho, Long> {
    
    /**
     * BUSCA HORÁRIOS DE TRABALHO POR PROFISSIONAL E DIA DA SEMANA
     * 
     * @param profissionalId ID do profissional
     * @param diaSemana Dia da semana
     * @return Lista de horários de trabalho ativos do profissional no dia especificado
     */
    List<HorarioTrabalho> findByProfissionalIdUsuarioAndDiaSemanaAndAtivoTrue(Long profissionalId, DayOfWeek diaSemana);
    
    /**
     * BUSCA HORÁRIOS DE TRABALHO ATIVOS POR PROFISSIONAL
     * 
     * @param profissionalId ID do profissional
     * @return Lista de todos os horários de trabalho ativos do profissional
     */
    List<HorarioTrabalho> findByProfissionalIdUsuarioAndAtivoTrue(Long profissionalId);
} 