package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

/**
 * ENTIDADE RECOMENDAÇÃO - REPRESENTA UMA RECOMENDAÇÃO PERSONALIZADA
 * 
 * Esta entidade representa uma recomendação personalizada baseada no critério
 * gerado pelo quiz do cliente. Cada recomendação possui um critério único e
 * uma descrição detalhada dos serviços recomendados.
 * 
 * CARACTERÍSTICAS:
 * - Critério único para identificação da recomendação
 * - Descrição detalhada dos serviços recomendados
 * - Status ativo/inativo para controle
 * - Timestamps de criação e atualização
 * 
 * USO:
 * - Baseado nas respostas do quiz do cliente
 * - Gera recomendações personalizadas de serviços
 * - Ajuda na escolha de profissionais e salões
 * 
 * @author João [Seu Nome]
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "recomendacoes")
public class Recomendacao {
    
    /**
     * ID ÚNICO DA RECOMENDAÇÃO
     * Chave primária auto-incrementada
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * CRITÉRIO DA RECOMENDAÇÃO
     * Critério único que identifica esta recomendação
     * Campo obrigatório e único para evitar duplicatas
     */
    @NotBlank(message = "O critério é obrigatório")
    @Column(nullable = false, unique = true, length = 100)
    private String criterio;
    
    /**
     * DESCRIÇÃO DA RECOMENDAÇÃO
     * Descrição detalhada dos serviços recomendados
     * Campo obrigatório com validação
     */
    @NotBlank(message = "A descrição da recomendação é obrigatória")
    @Column(nullable = false, length = 1000)
    private String descricao;
    
    /**
     * STATUS ATIVO DA RECOMENDAÇÃO
     * Controla se a recomendação está disponível
     * Valor padrão: true
     */
    @Column(nullable = false)
    private Boolean ativo = true;
    
    /**
     * DATA DE CRIAÇÃO DA RECOMENDAÇÃO
     * Timestamp de quando a recomendação foi criada
     * Não pode ser atualizado após a criação
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    /**
     * DATA DE ATUALIZAÇÃO DA RECOMENDAÇÃO
     * Timestamp da última atualização da recomendação
     */
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    /**
     * CONSTRUTOR PADRÃO
     */
    public Recomendacao() {}
    
    /**
     * CONSTRUTOR COMPLETO
     * 
     * @param id ID da recomendação
     * @param criterio Critério da recomendação
     * @param descricao Descrição da recomendação
     * @param ativo Status ativo
     * @param createdAt Data de criação
     * @param updatedAt Data de atualização
     */
    public Recomendacao(Long id, String criterio, String descricao, Boolean ativo, Date createdAt, Date updatedAt) {
        this.id = id;
        this.criterio = criterio;
        this.descricao = descricao;
        this.ativo = ativo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    /**
     * MÉTODO EXECUTADO ANTES DE PERSISTIR
     * Define automaticamente os timestamps de criação e atualização
     */
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }
    
    /**
     * MÉTODO EXECUTADO ANTES DE ATUALIZAR
     * Atualiza automaticamente o timestamp de atualização
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
    
    // ========== GETTERS E SETTERS ==========
    // Métodos para acessar e modificar os atributos da classe

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