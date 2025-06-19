package com.beauty.com.MatchBeauty.dto;

import com.beauty.com.MatchBeauty.entity.Usuario.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UsuarioDTO {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String username;
        private String password;
        private String nome;
        private String email;
        private String telefone;
        private TipoUsuario tipoUsuario;

        // Getters e Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getTelefone() { return telefone; }
        public void setTelefone(String telefone) { this.telefone = telefone; }
        public TipoUsuario getTipoUsuario() { return tipoUsuario; }
        public void setTipoUsuario(TipoUsuario tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long clienteId;
        private String username;
        private String nome;
        private String email;
        private String telefone;
        private TipoUsuario tipoUsuario;

        public Response(Long id, String nome, String email, String telefone, TipoUsuario tipoUsuario) {
            this.clienteId = id;
            this.nome = nome;
            this.email = email;
            this.telefone = telefone;
            this.tipoUsuario = tipoUsuario;
        }

        // Getters e Setters
        public Long getClienteId() { return clienteId; }
        public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getTelefone() { return telefone; }
        public void setTelefone(String telefone) { this.telefone = telefone; }
        public TipoUsuario getTipoUsuario() { return tipoUsuario; }
        public void setTipoUsuario(TipoUsuario tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    }
} 