package com.beauty.com.MatchBeauty.dto;

public class AprovarSalaoDTO {
    private Long salaoId;
    private String status; // "APROVADO" ou "REJEITADO"
    private String observacao; // Opcional, para justificar rejeição

    // Construtor padrão
    public AprovarSalaoDTO() {}

    // Construtor completo
    public AprovarSalaoDTO(Long salaoId, String status, String observacao) {
        this.salaoId = salaoId;
        this.status = status;
        this.observacao = observacao;
    }

    // Getters e Setters
    public Long getSalaoId() {
        return salaoId;
    }

    public void setSalaoId(Long salaoId) {
        this.salaoId = salaoId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
} 