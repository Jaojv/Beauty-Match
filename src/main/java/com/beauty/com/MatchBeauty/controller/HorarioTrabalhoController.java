package com.beauty.com.MatchBeauty.controller;

import com.beauty.com.MatchBeauty.service.ProfissionalService;
import com.beauty.com.MatchBeauty.repository.HorarioTrabalhoRepository;
import com.beauty.com.MatchBeauty.service.SalaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;
import com.beauty.com.MatchBeauty.dto.HorarioTrabalhoLoteDTO;

// Controller responsável por gerenciar horários de trabalho dos profissionais
// Permite cadastrar horários de trabalho em lote com validações
@RestController
@RequestMapping("/api/horarios-trabalho")
public class HorarioTrabalhoController {
    
    // Serviço para operações de profissional
    @Autowired
    private ProfissionalService profissionalService;
    
    // Repositório para operações de horário de trabalho
    @Autowired
    private HorarioTrabalhoRepository horarioTrabalhoRepository;
    
    // Serviço para operações de salão
    @Autowired
    private SalaoService salaoService;

    // Endpoint para cadastrar horários de trabalho em lote
    // Permite cadastrar múltiplos horários de uma vez com validações
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPRIETARIO', 'PROFISSIONAL')")
    @PostMapping("/lote")
    public ResponseEntity<?> cadastrarHorariosLote(@RequestBody List<HorarioTrabalhoLoteDTO> horarios) {
        // Validação dos horários antes de salvar
        List<String> erros = horarios.stream().map(h -> {
            // Validação básica - verifica se todos os campos obrigatórios estão presentes
            if (h.profissionalId == null || h.diaSemana == null || h.horaInicio == null || h.horaFim == null) {
                return "Campos obrigatórios ausentes";
            }
            
            // Validação do dia da semana
            DayOfWeek dia;
            try {
                dia = DayOfWeek.valueOf(h.diaSemana.toUpperCase());
            } catch (Exception e) {
                return "Dia da semana inválido: " + h.diaSemana;
            }
            
            // Validação do formato das horas
            LocalTime inicio, fim;
            try {
                inicio = LocalTime.parse(h.horaInicio);
                fim = LocalTime.parse(h.horaFim);
            } catch (Exception e) {
                return "Formato de hora inválido: " + h.horaInicio + " ou " + h.horaFim;
            }
            
            // Validação se hora de início é menor que hora de fim
            if (!inicio.isBefore(fim)) {
                return "Hora de início deve ser menor que hora de fim";
            }
            
            // Validação de funcionamento do salão
            var profissional = profissionalService.buscarProfissional(h.profissionalId);
            if (profissional == null) return "Profissional não encontrado: " + h.profissionalId;
            var salao = profissional.getSalao();
            if (salao == null) return "Profissional não está vinculado a um salão";
            
            // Verifica se o salão funciona no dia especificado
            var funcionamento = salao.getHorariosFuncionamento().stream()
                .filter(f -> f.getDiaSemana() == dia)
                .findFirst().orElse(null);
            if (funcionamento == null) return "Salão não funciona nesse dia: " + dia;
            
            // Verifica se o horário está dentro do funcionamento do salão
            if (inicio.isBefore(funcionamento.getHoraInicio()) || fim.isAfter(funcionamento.getHoraFim())) {
                return "Horário fora do funcionamento do salão: " + h.diaSemana;
            }
            return null;
        }).filter(e -> e != null).collect(Collectors.toList());
        
        // Se há erros, retorna lista de erros
        if (!erros.isEmpty()) {
            return ResponseEntity.badRequest().body(erros);
        }
        
        // Salvar horários válidos
        List<com.beauty.com.MatchBeauty.entity.HorarioTrabalho> salvos = horarios.stream().map(h -> {
            DayOfWeek dia = DayOfWeek.valueOf(h.diaSemana.toUpperCase());
            LocalTime inicio = LocalTime.parse(h.horaInicio);
            LocalTime fim = LocalTime.parse(h.horaFim);
            var profissional = profissionalService.buscarProfissional(h.profissionalId);
            
            // Cria novo horário de trabalho
            com.beauty.com.MatchBeauty.entity.HorarioTrabalho ht = new com.beauty.com.MatchBeauty.entity.HorarioTrabalho(profissional, dia, inicio, fim);
            ht.setAtivo(h.ativo != null ? h.ativo : true);
            ht.setBloqueado(h.bloqueado != null ? h.bloqueado : false);
            return horarioTrabalhoRepository.save(ht);
        }).collect(Collectors.toList());
        
        return ResponseEntity.status(201).body(salvos);
    }
} 