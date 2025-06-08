package com.beauty.com.MatchBeauty.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String nome;
        private String descricao;
        private BigDecimal preco;
        private Integer duracaoMinutos;
    }

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
} 