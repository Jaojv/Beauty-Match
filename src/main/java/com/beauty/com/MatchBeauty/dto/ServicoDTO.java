package com.beauty.com.MatchBeauty.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

public class ServicoDTO {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String nome;
        private String descricao;
        private BigDecimal preco;
        private Integer duracaoMinutos;
        private Long salaoId;
        private List<Long> profissionaisIds;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String nome;
        private String descricao;
        private BigDecimal preco;
        private Integer duracaoMinutos;
        private SalaoDTO.Response salao;
        private List<UsuarioDTO.Response> profissionais;
    }
} 