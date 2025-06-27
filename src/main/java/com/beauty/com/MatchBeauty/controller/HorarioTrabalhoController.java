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

@RestController
@RequestMapping("/api/horarios-trabalho")
public class HorarioTrabalhoController {
    @Autowired
    private ProfissionalService profissionalService;
    @Autowired
    private HorarioTrabalhoRepository horarioTrabalhoRepository;
    @Autowired
    private SalaoService salaoService;

    @PreAuthorize("hasAnyRole('ADMIN', 'PROPRIETARIO', 'PROFISSIONAL')")
    @PostMapping("/lote")
    public ResponseEntity<?> cadastrarHorariosLote(@RequestBody List<HorarioTrabalhoLoteDTO> horarios) {
        List<String> erros = horarios.stream().map(h -> {
            // Validação básica
            if (h.profissionalId == null || h.diaSemana == null || h.horaInicio == null || h.horaFim == null) {
                return "Campos obrigatórios ausentes";
            }
            DayOfWeek dia;
            try {
                dia = DayOfWeek.valueOf(h.diaSemana.toUpperCase());
            } catch (Exception e) {
                return "Dia da semana inválido: " + h.diaSemana;
            }
            LocalTime inicio, fim;
            try {
                inicio = LocalTime.parse(h.horaInicio);
                fim = LocalTime.parse(h.horaFim);
            } catch (Exception e) {
                return "Formato de hora inválido: " + h.horaInicio + " ou " + h.horaFim;
            }
            if (!inicio.isBefore(fim)) {
                return "Hora de início deve ser menor que hora de fim";
            }
            // Validação de funcionamento do salão
            var profissional = profissionalService.buscarProfissional(h.profissionalId);
            if (profissional == null) return "Profissional não encontrado: " + h.profissionalId;
            var salao = profissional.getSalao();
            if (salao == null) return "Profissional não está vinculado a um salão";
            var funcionamento = salao.getHorariosFuncionamento().stream()
                .filter(f -> f.getDiaSemana() == dia)
                .findFirst().orElse(null);
            if (funcionamento == null) return "Salão não funciona nesse dia: " + dia;
            if (inicio.isBefore(funcionamento.getHoraInicio()) || fim.isAfter(funcionamento.getHoraFim())) {
                return "Horário fora do funcionamento do salão: " + h.diaSemana;
            }
            return null;
        }).filter(e -> e != null).collect(Collectors.toList());
        if (!erros.isEmpty()) {
            return ResponseEntity.badRequest().body(erros);
        }
        // Salvar horários
        List<com.beauty.com.MatchBeauty.entity.HorarioTrabalho> salvos = horarios.stream().map(h -> {
            DayOfWeek dia = DayOfWeek.valueOf(h.diaSemana.toUpperCase());
            LocalTime inicio = LocalTime.parse(h.horaInicio);
            LocalTime fim = LocalTime.parse(h.horaFim);
            var profissional = profissionalService.buscarProfissional(h.profissionalId);
            com.beauty.com.MatchBeauty.entity.HorarioTrabalho ht = new com.beauty.com.MatchBeauty.entity.HorarioTrabalho(profissional, dia, inicio, fim);
            ht.setAtivo(h.ativo != null ? h.ativo : true);
            ht.setBloqueado(h.bloqueado != null ? h.bloqueado : false);
            return horarioTrabalhoRepository.save(ht);
        }).collect(Collectors.toList());
        return ResponseEntity.status(201).body(salvos);
    }
} 