package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.HorarioFuncionamentoSalao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface HorarioFuncionamentoSalaoRepository extends JpaRepository<HorarioFuncionamentoSalao, Long> {
    
    List<HorarioFuncionamentoSalao> findBySalaoIdAndAtivoTrue(Long salaoId);
    
    List<HorarioFuncionamentoSalao> findBySalaoIdAndDiaSemanaAndAtivoTrue(Long salaoId, DayOfWeek diaSemana);
    
    List<HorarioFuncionamentoSalao> findBySalaoId(Long salaoId);
    
    void deleteBySalaoId(Long salaoId);
} 