package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(name = "alternativas")
public class Alternativa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "O texto da alternativa é obrigatório")
    @Column(nullable = false, length = 200)
    private String texto;
    
    @NotNull(message = "A pergunta é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pergunta_id", nullable = false)
    private Pergunta pergunta;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    public Alternativa() {}
    
    public Alternativa(Long id, String texto, Pergunta pergunta, Boolean ativo, Date createdAt, Date updatedAt) {
        this.id = id;
        this.texto = texto;
        this.pergunta = pergunta;
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
    
    public String getTexto() {
        return texto;
    }
    
    public Pergunta getPergunta() {
        return pergunta;
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
    
    public void setTexto(String texto) {
        this.texto = texto;
    }
    
    public void setPergunta(Pergunta pergunta) {
        this.pergunta = pergunta;
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