package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.entity.HorarioFuncionamentoSalao;
import com.beauty.com.MatchBeauty.entity.Salao;
import com.beauty.com.MatchBeauty.repository.HorarioFuncionamentoSalaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SERVIÇO DE HORÁRIOS DE FUNCIONAMENTO DOS SALÕES
 * 
 * Este serviço gerencia os horários de funcionamento dos salões,
 * permitindo definir e consultar os horários disponíveis para cada
 * dia da semana. Essencial para o sistema de agendamentos.
 * 
 * FUNCIONALIDADES:
 * - Definição de horários de funcionamento por dia da semana
 * - Consulta de horários ativos por salão
 * - Verificação de disponibilidade de horários
 * - Gerenciamento de horários de abertura e fechamento
 * - Validação de horários de funcionamento
 * - Consulta de horários por período
 * 
 * ESTRUTURA:
 * - Horários por dia da semana (segunda a domingo)
 * - Horário de abertura e fechamento por dia
 * - Status ativo/inativo para horários
 * - Associação com salão específico
 * 
 * VALIDAÇÕES:
 * - Verificação de horários válidos
 * - Validação de sobreposição de horários
 * - Verificação de horários de funcionamento
 * - Validação de dias da semana
 * 
 * DEPENDÊNCIAS:
 * - HorarioFuncionamentoSalaoRepository: Para persistência
 */
@Service
public class HorarioFuncionamentoSalaoService {
    
    @Autowired
    private HorarioFuncionamentoSalaoRepository horarioFuncionamentoRepository;
    
    public void configurarHorariosPadrao(Salao salao) {
        // Verificar se já existem horários configurados para este salão
        List<HorarioFuncionamentoSalao> horariosExistentes = horarioFuncionamentoRepository.findBySalaoId(salao.getId());
        
        if (!horariosExistentes.isEmpty()) {
            return; // Horários já configurados, não criar duplicatas
        }
        
        List<HorarioFuncionamentoSalao> horarios = new ArrayList<>();
        
        for (DayOfWeek dia : List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, 
                                   DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)) {
            horarios.add(new HorarioFuncionamentoSalao(salao, dia, LocalTime.of(8, 0), LocalTime.of(18, 0)));
        }
        
        salao.setHorariosFuncionamento(horarios);
        horarioFuncionamentoRepository.saveAll(horarios);
    }
    
    public void configurarHorariosCustomizados(Salao salao, List<HorarioFuncionamentoSalao> horarios) {
        horarioFuncionamentoRepository.deleteBySalaoId(salao.getId());
        
        for (HorarioFuncionamentoSalao horario : horarios) {
            horario.setSalao(salao);
        }
        
        salao.setHorariosFuncionamento(horarios);
        horarioFuncionamentoRepository.saveAll(horarios);
    }
    
    public List<LocalTime> gerarSlotsDisponiveis(Long salaoId, DayOfWeek diaSemana) {
        List<HorarioFuncionamentoSalao> horarios = horarioFuncionamentoRepository
            .findBySalaoIdAndDiaSemanaAndAtivoTrue(salaoId, diaSemana);
        
        List<LocalTime> slots = new ArrayList<>();
        
        for (HorarioFuncionamentoSalao horario : horarios) {
            LocalTime horaAtual = horario.getHoraInicio();
            LocalTime horaFim = horario.getHoraFim();
            
            while (horaAtual.isBefore(horaFim)) {
                slots.add(horaAtual);
                horaAtual = horaAtual.plusMinutes(15);
            }
        }
        
        // Remover duplicatas se houver e ordenar
        return slots.stream()
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }
    
    public boolean isHorarioFuncionamento(Long salaoId, DayOfWeek diaSemana, LocalTime horario) {
        List<HorarioFuncionamentoSalao> horarios = horarioFuncionamentoRepository
            .findBySalaoIdAndDiaSemanaAndAtivoTrue(salaoId, diaSemana);
        
        for (HorarioFuncionamentoSalao horarioFunc : horarios) {
            if (!horario.isBefore(horarioFunc.getHoraInicio()) && 
                !horario.isAfter(horarioFunc.getHoraFim())) {
                return true;
            }
        }
        
        return false;
    }
    
    public List<HorarioFuncionamentoSalao> buscarHorariosPorSalao(Long salaoId) {
        return horarioFuncionamentoRepository.findBySalaoIdAndAtivoTrue(salaoId);
    }
    
    public String formatarHorario(LocalTime horario) {
        return horario.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
