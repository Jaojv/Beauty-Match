package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.Agendamento;
import com.beauty.com.MatchBeauty.entity.Agendamento.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    
    List<Agendamento> findByClienteIdUsuario(Long clienteId);
    
    List<Agendamento> findByProfissionalIdUsuario(Long profissionalId);
    
    List<Agendamento> findBySalaoId(Long salaoId);
    
    List<Agendamento> findByStatus(StatusAgendamento status);
    
    List<Agendamento> findByClienteIdUsuarioAndDataHoraBetween(
        Long clienteId, 
        LocalDateTime inicio, 
        LocalDateTime fim
    );
    
    List<Agendamento> findByProfissionalIdUsuarioAndDataHoraBetween(
        Long profissionalId, 
        LocalDateTime inicio, 
        LocalDateTime fim
    );
    
    List<Agendamento> findBySalaoIdAndDataHoraBetween(
        Long salaoId, 
        LocalDateTime inicio, 
        LocalDateTime fim
    );
} 