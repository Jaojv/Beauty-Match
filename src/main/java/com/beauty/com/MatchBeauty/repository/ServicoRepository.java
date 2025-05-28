package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    
    List<Servico> findBySalaoId(Long salaoId);
    
    List<Servico> findByProfissionaisIdUsuario(Long profissionalId);
    
    Optional<Servico> findByNomeAndSalaoId(String nome, Long salaoId);
    
    boolean existsByNomeAndSalaoId(String nome, Long salaoId);
} 