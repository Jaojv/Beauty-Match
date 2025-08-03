package com.beauty.com.MatchBeauty.dto;

/**
 * DTO para listagem de usuários no painel administrativo
 * Contém informações básicas de todos os tipos de usuários
 */
public class UsuarioAdminDTO {
    
    private Long id;
    private String username;
    private String nome;
    private String email;
    private String telefone;
    private String tipoUsuario;
    private String status;

    // Construtor padrão
    public UsuarioAdminDTO() {}

    // Construtor com todos os campos
    public UsuarioAdminDTO(Long id, String username, String nome, String email, 
                          String telefone, String tipoUsuario, String status) {
        this.id = id;
        this.username = username;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.tipoUsuario = tipoUsuario;
        this.status = status;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 