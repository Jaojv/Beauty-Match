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
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String username;
        private String nome;
        private String email;
        private String telefone;
        private TipoUsuario tipoUsuario;
    }
} 