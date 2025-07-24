package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * REPOSITÓRIO ADMIN - ACESSO A DADOS DE ADMINISTRADORES
 * 
 * Este repositório fornece métodos para acessar e manipular dados de administradores
 * no sistema Match Beauty. Estende JpaRepository para herdar operações CRUD básicas.
 * 
 * FUNCIONALIDADES:
 * - Operações CRUD básicas (herdadas de JpaRepository)
 * - Gerenciamento de administradores
 * - Consulta de dados de administradores
 * 
 * USO:
 * - Cadastro e atualização de administradores
 * - Consulta de informações de administradores
 * - Gerenciamento de perfis de administradores
 *
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
} 