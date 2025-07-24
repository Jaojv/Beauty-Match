package com.beauty.com.MatchBeauty.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ENTIDADE ADMIN - REPRESENTA UM ADMINISTRADOR DO SISTEMA
 * 
 * Esta entidade representa um administrador no sistema Match Beauty.
 * Herda da classe Usuario e adiciona informações específicas de administradores
 * como nível de acesso para controle de permissões.
 * 
 * HERANÇA:
 * - Estende a classe Usuario (herança JOINED)
 * - Herda todos os campos básicos: id, username, password, email, etc.
 * - Adiciona campos específicos de administrador
 * 
 * RELACIONAMENTOS:
 * - Um admin pode gerenciar todos os usuários do sistema
 * - Um admin pode acessar relatórios e configurações
 * 
 * CARACTERÍSTICAS ESPECÍFICAS:
 * - Nível de acesso para controle de permissões
 * - Acesso total ao sistema
 * - Capacidade de gerenciar outros usuários
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "admin")
public class Admin extends Usuario {
    
    /**
     * NÍVEL DE ACESSO DO ADMINISTRADOR
     * Define as permissões específicas do administrador
     * Ex: "SUPER_ADMIN", "ADMIN", "MODERATOR"
     */
    @Column(name = "nivel_acesso")
    private String nivelAcesso;
    
    /**
     * CONSTRUTOR PADRÃO
     * Chama o construtor da classe pai (Usuario)
     */
    public Admin() {
        super();
    }

    /**
     * CONSTRUTOR COM PARÂMETROS BÁSICOS
     * 
     * @param idUsuario ID do usuário
     * @param username Nome de usuário
     * @param password Senha
     * @param email Email
     * @param telefone Telefone
     * @param criadoEm Data de criação
     * @param atualizadoEm Data de atualização
     */
    public Admin(Long idUsuario, String username, String password, String email, String telefone, 
                LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        super(idUsuario, username, password, email, telefone, criadoEm, atualizadoEm);
    }

    // ========== GETTERS E SETTERS ==========
    // Métodos para acessar e modificar os atributos específicos de Admin

    public String getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(String nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }

} 