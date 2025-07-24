package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.Proprietario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * REPOSITÓRIO PROPRIETÁRIO - ACESSO A DADOS DE PROPRIETÁRIOS
 * 
 * Este repositório fornece métodos para acessar e manipular dados de proprietários
 * no sistema Match Beauty. Estende JpaRepository para herdar operações CRUD básicas.
 * 
 * FUNCIONALIDADES:
 * - Operações CRUD básicas (herdadas de JpaRepository)
 * - Gerenciamento de proprietários
 * - Consulta de dados de proprietários
 * 
 * USO:
 * - Cadastro e atualização de proprietários
 * - Consulta de informações de proprietários
 * - Gerenciamento de perfis de proprietários
 *
 */
@Repository
public interface ProprietarioRepository extends JpaRepository<Proprietario, Long> {
} 