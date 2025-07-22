package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ENTIDADE PERGUNTA - REPRESENTA UMA PERGUNTA DO QUIZ
 * 
 * Esta entidade representa uma pergunta do quiz de personalização do sistema.
 * Cada pergunta possui texto, ordem de exibição, alternativas e status ativo.
 * 
 * RELACIONAMENTOS:
 * - Uma pergunta pode ter várias alternativas (OneToMany com Alternativa)
 * - Uma pergunta pode ter várias respostas (OneToMany via RespostaQuiz)
 * 
 * CARACTERÍSTICAS:
 * - Texto da pergunta com validação obrigatória
 * - Ordem de exibição para sequência do quiz
 * - Lista de alternativas ordenadas
 * - Status ativo/inativo para controle
 * - Timestamps de criação e atualização
 *
 */
@Entity
@Table(name = "perguntas")
public class Pergunta {
    
    /**
     * ID ÚNICO DA PERGUNTA
     * Chave primária auto-incrementada
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * TEXTO DA PERGUNTA
     * Pergunta que será exibida para o usuário
     * Campo obrigatório com validação
     */
    @NotBlank(message = "O texto da pergunta é obrigatório")
    @Column(nullable = false, length = 500)
    private String texto;
    
    /**
     * ORDEM DA PERGUNTA
     * Define a sequência de exibição das perguntas no quiz
     * Campo obrigatório para controle de fluxo
     */
    @NotNull(message = "A ordem da pergunta é obrigatória")
    @Column(nullable = false)
    private Integer ordem;
    
    /**
     * ALTERNATIVAS DA PERGUNTA
     * Lista de opções de resposta para a pergunta
     * Relacionamento OneToMany com Alternativa
     * Carregamento EAGER para exibição completa
     */
    @OneToMany(mappedBy = "pergunta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("id ASC")
    private List<Alternativa> alternativas = new ArrayList<>();
    
    /**
     * STATUS ATIVO DA PERGUNTA
     * Controla se a pergunta está disponível no quiz
     * Valor padrão: true
     */
    @Column(nullable = false)
    private Boolean ativo = true;
    
    /**
     * DATA DE CRIAÇÃO DA PERGUNTA
     * Timestamp de quando a pergunta foi criada
     * Não pode ser atualizado após a criação
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    /**
     * DATA DE ATUALIZAÇÃO DA PERGUNTA
     * Timestamp da última atualização da pergunta
     */
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    /**
     * CONSTRUTOR PADRÃO
     */
    public Pergunta() {}
    
    /**
     * CONSTRUTOR COMPLETO
     * 
     * @param id ID da pergunta
     * @param texto Texto da pergunta
     * @param ordem Ordem de exibição
     * @param alternativas Lista de alternativas
     * @param ativo Status ativo
     * @param createdAt Data de criação
     * @param updatedAt Data de atualização
     */
    public Pergunta(Long id, String texto, Integer ordem, List<Alternativa> alternativas, Boolean ativo, Date createdAt, Date updatedAt) {
        this.id = id;
        this.texto = texto;
        this.ordem = ordem;
        this.alternativas = alternativas;
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