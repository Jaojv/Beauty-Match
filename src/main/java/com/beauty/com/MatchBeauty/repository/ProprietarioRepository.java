package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.Proprietario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProprietarioRepository extends JpaRepository<Proprietario, Long> {
} 