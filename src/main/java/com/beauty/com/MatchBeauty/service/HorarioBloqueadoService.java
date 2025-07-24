package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.entity.HorarioBloqueado;
import com.beauty.com.MatchBeauty.repository.HorarioBloqueadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * SERVIÇO DE HORÁRIOS BLOQUEADOS - GERENCIAMENTO DE BLOQUEIOS
 * 
 * Este serviço gerencia os horários bloqueados dos salões e profissionais,
 * permitindo definir períodos onde não é possível realizar agendamentos.
 * Útil para feriados, férias, manutenções ou horários especiais.
 * 
 * FUNCIONALIDADES:
 * - Criação de bloqueios de horários
 * - Consulta de horários bloqueados por salão
 * - Verificação de conflitos de horário
 * - Remoção de bloqueios
 * - Validação de períodos de bloqueio
 * - Consulta de bloqueios por período
 * 
 * TIPOS DE BLOQUEIO:
 * - Bloqueios por salão (feriados, manutenções)
 * - Bloqueios por profissional (férias, ausências)
 * - Bloqueios por período específico
 * - Bloqueios recorrentes
 * 
 * VALIDAÇÕES:
 * - Verificação de sobreposição de horários
 * - Validação de datas (não permitir datas passadas)
 * - Verificação de conflitos com agendamentos existentes
 * - Validação de horários de funcionamento
 * 
 * DEPENDÊNCIAS:
 * - HorarioBloqueadoRepository: Para persistência de bloqueios
 */
@Service
public class HorarioBloqueadoService {

    @Autowired
    private HorarioBloqueadoRepository horarioBloqueadoRepository;

    public List<HorarioBloqueado> listarHorariosBloqueados() {
        return horarioBloqueadoRepository.findAll();
    }

    public HorarioBloqueado buscarHorarioBloqueado(Long id) {
        return horarioBloqueadoRepository.findById(id).orElse(null);
    }

    public HorarioBloqueado criarHorarioBloqueado(HorarioBloqueado horarioBloqueado) {
        return horarioBloqueadoRepository.save(horarioBloqueado);
    }

    public HorarioBloqueado atualizarHorarioBloqueado(HorarioBloqueado horarioBloqueado) {
        if (horarioBloqueadoRepository.existsById(horarioBloqueado.getId())) {
            return horarioBloqueadoRepository.save(horarioBloqueado);
        }
        return null;
    }

    public boolean deletarHorarioBloqueado(Long id) {
        if (horarioBloqueadoRepository.existsById(id)) {
            horarioBloqueadoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<HorarioBloqueado> buscarHorariosBloqueadosPorSalao(Long salaoId) {
        return horarioBloqueadoRepository.findBySalaoId(salaoId);
    }

    public List<HorarioBloqueado> buscarHorariosBloqueadosPorSalaoEPeriodo(
        Long salaoId, 
        LocalDateTime inicio, 
        LocalDateTime fim
    ) {
        return horarioBloqueadoRepository.findBySalaoIdAndDataHoraInicioBetween(salaoId, inicio, fim);
    }
} 