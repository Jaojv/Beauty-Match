package com.beauty.com.MatchBeauty.dto;

public class LoginResponse {
    private String token;
    private Long idUsuario;
    private String username;
    private String tipoUsuario;

    public LoginResponse() {
    }

    public LoginResponse(String token, Long idUsuario, String username, String tipoUsuario) {
        this.token = token;
        this.idUsuario = idUsuario;
        this.username = username;
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

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
} 