package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.Profissional;
import com.beauty.com.MatchBeauty.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    Optional<Profissional> findByUsuario(Usuario usuario);
} 