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

@Service
public class HorarioFuncionamentoSalaoService {
    
    @Autowired
    private HorarioFuncionamentoSalaoRepository horarioFuncionamentoRepository;
    
    public void configurarHorariosPadrao(Salao salao) {
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
        
        return slots;
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
