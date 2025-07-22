package com.beauty.com.MatchBeauty.entity;

/**
 * ENUM ROLE - DEFINE OS PAPÉIS DOS USUÁRIOS NO SISTEMA
 * 
 * Este enum define os diferentes papéis que um usuário pode ter no sistema Match Beauty.
 * Cada papel possui permissões e funcionalidades específicas.
 * 
 * PAPÉIS DISPONÍVEIS:
 * - ADMIN: Administrador do sistema com acesso total
 * - PROPRIETARIO: Proprietário de salão com acesso gerencial
 * - PROFISSIONAL: Profissional que presta serviços
 * - CLIENTE: Cliente que agenda serviços
 *
 */
public enum Role {
    ADMIN,          // Administrador do sistema
    PROPRIETARIO,   // Proprietário de salão
    PROFISSIONAL,   // Profissional de beleza
    CLIENTE         // Cliente do sistema
} 