package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.entity.Agendamento;
import com.beauty.com.MatchBeauty.entity.Agendamento.StatusAgendamento;
import com.beauty.com.MatchBeauty.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    public List<Agendamento> listarAgendamentos() {
        return agendamentoRepository.findAll();
    }

    public Agendamento buscarAgendamento(Long id) {
        return agendamentoRepository.findById(id).orElse(null);
    }

    public Agendamento criarAgendamento(Agendamento agendamento) {
        agendamento.setStatus(StatusAgendamento.AGENDADO);
        return agendamentoRepository.save(agendamento);
    }

    public Agendamento atualizarAgendamento(Agendamento agendamento) {
        if (agendamentoRepository.existsById(agendamento.getId())) {
            return agendamentoRepository.save(agendamento);
        }
        return null;
    }

    public boolean deletarAgendamento(Long id) {
        if (agendamentoRepository.existsById(id)) {
            agendamentoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Agendamento> buscarAgendamentosPorCliente(Long clienteId) {
        return agendamentoRepository.findByClienteIdUsuario(clienteId);
    }

    public List<Agendamento> buscarAgendamentosPorProfissional(Long profissionalId) {
        return agendamentoRepository.findByProfissionalIdUsuario(profissionalId);
    }

    public List<Agendamento> buscarAgendamentosPorSalao(Long salaoId) {
        return agendamentoRepository.findBySalaoId(salaoId);
    }

    public List<Agendamento> buscarAgendamentosPorStatus(StatusAgendamento status) {
        return agendamentoRepository.findByStatus(status);
    }

    public List<Agendamento> buscarAgendamentosPorClienteEPeriodo(
        Long clienteId, 
        LocalDateTime inicio, 
        LocalDateTime fim
    ) {
        return agendamentoRepository.findByClienteIdUsuarioAndDataHoraBetween(clienteId, inicio, fim);
    }

    public List<Agendamento> buscarAgendamentosPorProfissionalEPeriodo(
        Long profissionalId, 
        LocalDateTime inicio, 
        LocalDateTime fim
    ) {
        return agendamentoRepository.findByProfissionalIdUsuarioAndDataHoraBetween(profissionalId, inicio, fim);
    }

    public List<Agendamento> buscarAgendamentosPorSalaoEPeriodo(
        Long salaoId, 
        LocalDateTime inicio, 
        LocalDateTime fim
    ) {
        return agendamentoRepository.findBySalaoIdAndDataHoraBetween(salaoId, inicio, fim);
    }
} 