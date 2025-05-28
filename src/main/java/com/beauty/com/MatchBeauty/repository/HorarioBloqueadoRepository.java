package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.HorarioBloqueado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HorarioBloqueadoRepository extends JpaRepository<HorarioBloqueado, Long> {
    List<HorarioBloqueado> findBySalaoId(Long salaoId);
    List<HorarioBloqueado> findBySalaoIdAndDataHoraInicioBetween(Long salaoId, LocalDateTime inicio, LocalDateTime fim);
} 