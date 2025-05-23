package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
} 