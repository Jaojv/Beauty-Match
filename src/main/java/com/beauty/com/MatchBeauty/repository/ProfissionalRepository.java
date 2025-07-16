package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    // Métodos específicos do Profissional podem ser adicionados aqui
    List<Profissional> findBySalao_Id(Long salaoId);
} 