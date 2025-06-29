package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "respostas_quiz")
public class RespostaQuiz {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "O cliente é obrigatório")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false, unique = true)
    private Cliente cliente;
    
    @ElementCollection
    @CollectionTable(
        name = "respostas_quiz_detalhes",
        joinColumns = @JoinColumn(name = "resposta_quiz_id")
    )
    @MapKeyColumn(name = "pergunta")
    @Column(name = "resposta", length = 200)
    private Map<String, String> respostas = new HashMap<>();
    
    @Column(name = "criterio_gerado", length = 100)
    private String criterioGerado;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    public RespostaQuiz() {}
    
    public RespostaQuiz(Long id, Cliente cliente, Map<String, String> respostas, String criterioGerado, Date createdAt, Date updatedAt) {
        this.id = id;
        this.cliente = cliente;
        this.respostas = respostas;
        this.criterioGerado = criterioGerado;
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
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public Map<String, String> getRespostas() {
        return respostas;
    }
    
    public String getCriterioGerado() {
        return criterioGerado;
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
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public void setRespostas(Map<String, String> respostas) {
        this.respostas = respostas;
    }
    
    public void setCriterioGerado(String criterioGerado) {
        this.criterioGerado = criterioGerado;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
} 