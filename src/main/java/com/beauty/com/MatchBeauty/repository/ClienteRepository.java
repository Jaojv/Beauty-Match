package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
/**
 * REPOSITÓRIO CLIENTE - ACESSO A DADOS DE CLIENTES
 * 
 * Este repositório fornece métodos para acessar e manipular dados de clientes
 * no sistema Match Beauty. Estende JpaRepository para herdar operações CRUD básicas.
 * 
 * FUNCIONALIDADES:
 * - Operações CRUD básicas (herdadas de JpaRepository)
 * - Gerenciamento de clientes
 * - Consulta de dados de clientes
 * 
 * USO:
 * - Cadastro e atualização de clientes
 * - Consulta de informações de clientes
 * - Gerenciamento de perfis de clientes
 *
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
} 