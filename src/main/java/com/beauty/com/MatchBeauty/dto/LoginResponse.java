package com.beauty.com.MatchBeauty.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {
    private String token;
    @JsonProperty("idUsuario")
    private Long idUsuario;
    private String username;
    private String nome;
    private String email;
    private String tipoUsuario;

    public LoginResponse() {
    }

    public LoginResponse(String token, Long idUsuario, String username, String nome, String email, String tipoUsuario) {
        this.token = token;
        this.idUsuario = idUsuario;
        this.username = username;
        this.nome = nome;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
} 