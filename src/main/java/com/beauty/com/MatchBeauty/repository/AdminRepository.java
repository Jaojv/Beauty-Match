package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
} 