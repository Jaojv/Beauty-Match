package com.beauty.com.MatchBeauty.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SalaoDTO {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String nome;
        private String endereco;
        private String telefone;
        private String descricao;
        private String horarioFuncionamento;
        private Long proprietarioId;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String nome;
        private String endereco;
        private String telefone;
        private String descricao;
        private String horarioFuncionamento;
        private UsuarioDTO.Response proprietario;
    }
} 