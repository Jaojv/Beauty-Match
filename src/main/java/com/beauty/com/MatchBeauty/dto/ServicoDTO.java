package com.beauty.com.MatchBeauty.dto;

import java.math.BigDecimal;
import java.util.List;

public class ServicoDTO {
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer duracaoMinutos;
    private Long salaoId;
    private List<Long> profissionaisIds;

    public ServicoDTO() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Integer getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public void setDuracaoMinutos(Integer duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }

    public Long getSalaoId() {
        return salaoId;
    }

    public void setSalaoId(Long salaoId) {
        this.salaoId = salaoId;
    }

    public List<Long> getProfissionaisIds() {
        return profissionaisIds;
    }

    public void setProfissionaisIds(List<Long> profissionaisIds) {
        this.profissionaisIds = profissionaisIds;
    }

    
    public static class Response {
        private Long id;
        private String nome;
        private String descricao;
        private BigDecimal preco;
        private Integer duracaoMinutos;
        private Long salaoId;

        public Response() {
        }

        public Response(Long id, String nome, String descricao, BigDecimal preco, Integer duracaoMinutos, Long salaoId) {
            this.id = id;
            this.nome = nome;
            this.descricao = descricao;
            this.preco = preco;
            this.duracaoMinutos = duracaoMinutos;
            this.salaoId = salaoId;
        }

        public Response(com.beauty.com.MatchBeauty.entity.Servico servico) {
            this.id = servico.getId();
            this.nome = servico.getNome();
            this.descricao = servico.getDescricao();
            this.preco = servico.getPreco();
            this.duracaoMinutos = servico.getDuracaoMinutos();
            this.salaoId = servico.getSalao() != null ? servico.getSalao().getId() : null;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public BigDecimal getPreco() {
            return preco;
        }

        public void setPreco(BigDecimal preco) {
            this.preco = preco;
        }

        public Integer getDuracaoMinutos() {
            return duracaoMinutos;
        }

        public void setDuracaoMinutos(Integer duracaoMinutos) {
            this.duracaoMinutos = duracaoMinutos;
        }

        public Long getSalaoId() {
            return salaoId;
        }

        public void setSalaoId(Long salaoId) {
            this.salaoId = salaoId;
        }
    }

    public static class Request {
        private String nome;
        private String descricao;
        private BigDecimal preco;
        private Integer duracaoMinutos;
        private Long salaoId;
        private List<Long> profissionaisIds;
    }
} 