package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

@Entity
@Table(name = "recomendacoes")
public class Recomendacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "O critério é obrigatório")
    @Column(nullable = false, unique = true, length = 100)
    private String criterio;
    
    @NotBlank(message = "A descrição da recomendação é obrigatória")
    @Column(nullable = false, length = 1000)
    private String descricao;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    public Recomendacao() {}
    
    public Recomendacao(Long id, String criterio, String descricao, Boolean ativo, Date createdAt, Date updatedAt) {
        this.id = id;
        this.criterio = criterio;
        this.descricao = descricao;
        this.ativo = ativo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public String getCriterio() {
        return criterio;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public Boolean getAtivo() {
        return ativo;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
} 