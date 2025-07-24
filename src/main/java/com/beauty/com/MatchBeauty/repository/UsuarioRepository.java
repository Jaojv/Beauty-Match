package com.beauty.com.MatchBeauty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beauty.com.MatchBeauty.entity.Usuario;
import java.util.Optional;

/**
 * REPOSITÓRIO USUÁRIO - ACESSO A DADOS DE USUÁRIOS
 * 
 * Este repositório fornece métodos para acessar e manipular dados de usuários
 * no sistema Match Beauty. Estende JpaRepository para herdar operações CRUD básicas
 * e adiciona métodos específicos para busca por username e email.
 * 
 * FUNCIONALIDADES:
 * - Operações CRUD básicas (herdadas de JpaRepository)
 * - Busca de usuário por nome de usuário
 * - Busca de usuário por email
 * - Validação de unicidade de username e email
 * 
 * USO:
 * - Autenticação e autorização
 * - Validação de dados únicos
 * - Gerenciamento de usuários
 *
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * BUSCA USUÁRIO POR NOME DE USUÁRIO
     * 
     * @param username Nome de usuário para busca
     * @return Optional contendo o usuário encontrado ou vazio se não encontrado
     */
    Optional<Usuario> findByUsername(String username);
    
    /**
     * BUSCA USUÁRIO POR EMAIL
     * 
     * @param email Email para busca
     * @return Optional contendo o usuário encontrado ou vazio se não encontrado
     */
    Optional<Usuario> findByEmail(String email);
} 