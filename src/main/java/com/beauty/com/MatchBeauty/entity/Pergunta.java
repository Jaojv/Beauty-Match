package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "perguntas")
public class Pergunta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "O texto da pergunta é obrigatório")
    @Column(nullable = false, length = 500)
    private String texto;
    
    @NotNull(message = "A ordem da pergunta é obrigatória")
    @Column(nullable = false)
    private Integer ordem;
    
    @OneToMany(mappedBy = "pergunta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("id ASC")
    private List<Alternativa> alternativas = new ArrayList<>();
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    public Pergunta() {}
    
    public Pergunta(Long id, String texto, Integer ordem, List<Alternativa> alternativas, Boolean ativo, Date createdAt, Date updatedAt) {
        this.id = id;
        this.texto = texto;
        this.ordem = ordem;
        this.alternativas = alternativas;
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
    
    public Integer getOrdem() {
        return ordem;
    }
    
    public List<Alternativa> getAlternativas() {
        return alternativas;
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
    
    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }
    
    public void setAlternativas(List<Alternativa> alternativas) {
        this.alternativas = alternativas;
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