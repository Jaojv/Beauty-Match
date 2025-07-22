package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

/**
 * ENTIDADE ALTERNATIVA - REPRESENTA UMA ALTERNATIVA DE RESPOSTA
 * 
 * Esta entidade representa uma alternativa de resposta para uma pergunta do quiz.
 * Cada alternativa possui texto, pertence a uma pergunta e tem status ativo.
 * 
 * RELACIONAMENTOS:
 * - Uma alternativa pertence a uma pergunta (ManyToOne com Pergunta)
 * - Uma alternativa pode ter várias respostas (OneToMany via RespostaQuiz)
 * 
 * CARACTERÍSTICAS:
 * - Texto da alternativa com validação obrigatória
 * - Relacionamento com pergunta específica
 * - Status ativo/inativo para controle
 * - Timestamps de criação e atualização
 *
 */
@Entity
@Table(name = "alternativas")
public class Alternativa {
    
    /**
     * ID ÚNICO DA ALTERNATIVA
     * Chave primária auto-incrementada
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * TEXTO DA ALTERNATIVA
     * Opção de resposta que será exibida para o usuário
     * Campo obrigatório com validação
     */
    @NotBlank(message = "O texto da alternativa é obrigatório")
    @Column(nullable = false, length = 200)
    private String texto;
    
    /**
     * PERGUNTA DA ALTERNATIVA
     * Pergunta à qual esta alternativa pertence
     * Relacionamento ManyToOne com Pergunta
     * Carregamento LAZY para otimizar performance
     */
    @NotNull(message = "A pergunta é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pergunta_id", nullable = false)
    private Pergunta pergunta;
    
    /**
     * STATUS ATIVO DA ALTERNATIVA
     * Controla se a alternativa está disponível para seleção
     * Valor padrão: true
     */
    @Column(nullable = false)
    private Boolean ativo = true;
    
    /**
     * DATA DE CRIAÇÃO DA ALTERNATIVA
     * Timestamp de quando a alternativa foi criada
     * Não pode ser atualizado após a criação
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    /**
     * DATA DE ATUALIZAÇÃO DA ALTERNATIVA
     * Timestamp da última atualização da alternativa
     */
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    /**
     * CONSTRUTOR PADRÃO
     */
    public Alternativa() {}
    
    /**
     * CONSTRUTOR COMPLETO
     * 
     * @param id ID da alternativa
     * @param texto Texto da alternativa
     * @param pergunta Pergunta da alternativa
     * @param ativo Status ativo
     * @param createdAt Data de criação
     * @param updatedAt Data de atualização
     */
    public Alternativa(Long id, String texto, Pergunta pergunta, Boolean ativo, Date createdAt, Date updatedAt) {
        this.id = id;
        this.texto = texto;
        this.pergunta = pergunta;
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