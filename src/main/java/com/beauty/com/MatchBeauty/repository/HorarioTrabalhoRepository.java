package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.HorarioTrabalho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface HorarioTrabalhoRepository extends JpaRepository<HorarioTrabalho, Long> {
    
    List<HorarioTrabalho> findByProfissionalIdUsuarioAndDiaSemanaAndAtivoTrue(Long profissionalId, DayOfWeek diaSemana);
    
    List<HorarioTrabalho> findByProfissionalIdUsuarioAndAtivoTrue(Long profissionalId);
} 