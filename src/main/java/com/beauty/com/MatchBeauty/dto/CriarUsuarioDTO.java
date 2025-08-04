package com.beauty.com.MatchBeauty.dto;

/**
 * DTO para criação de usuários pelo administrador
 * Contém todos os dados necessários para criar um novo usuário
 */
public class CriarUsuarioDTO {
    
    private String username;
    private String password;
    private String nome;
    private String email;
    private String telefone;
    private String tipoUsuario;

    // Construtor padrão
    public CriarUsuarioDTO() {}

    // Construtor com todos os campos
    public CriarUsuarioDTO(String username, String password, String nome, 
                          String email, String telefone, String tipoUsuario) {
        this.username = username;
        this.password = password;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.tipoUsuario = tipoUsuario;
    }

    // Getters e Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
} 