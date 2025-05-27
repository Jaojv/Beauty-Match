package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.Salao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalaoRepository extends JpaRepository<Salao, Long> {
    
    List<Salao> findByProprietarioId(Long proprietarioId);
    
    Optional<Salao> findByNomeAndEndereco(String nome, String endereco);
    
    boolean existsByNomeAndEndereco(String nome, String endereco);
} 