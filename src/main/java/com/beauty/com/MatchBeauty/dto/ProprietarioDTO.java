package com.beauty.com.MatchBeauty.dto;

public class ProprietarioDTO {
    private Long idUsuario;
    private String username;
    private String nome;
    private String email;
    private String telefone;

    public ProprietarioDTO() {
    }

    public ProprietarioDTO(Long idUsuario, String username, String nome, String email, String telefone) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
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
} 