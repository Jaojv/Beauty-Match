package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ENTIDADE RESPOSTA QUIZ - REPRESENTA AS RESPOSTAS DE UM CLIENTE NO QUIZ
 * 
 * Esta entidade representa as respostas de um cliente no quiz de personalização.
 * Armazena todas as respostas de um cliente em um mapa e gera um critério
 * baseado nessas respostas para recomendações.
 * 
 * RELACIONAMENTOS:
 * - Uma resposta pertence a um cliente (OneToOne com Cliente)
 * - Uma resposta pode ter várias entradas no mapa de respostas
 * 
 * CARACTERÍSTICAS:
 * - Relacionamento único com cliente (OneToOne)
 * - Mapa de respostas para perguntas específicas
 * - Critério gerado automaticamente baseado nas respostas
 * - Timestamps de criação e atualização
 *
 */
@Entity
@Table(name = "respostas_quiz")
public class RespostaQuiz {
    
    /**
     * ID ÚNICO DA RESPOSTA QUIZ
     * Chave primária auto-incrementada
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * CLIENTE DA RESPOSTA QUIZ
     * Cliente que respondeu o quiz
     * Relacionamento OneToOne único com Cliente
     * Carregamento LAZY para otimizar performance
     */
    @NotNull(message = "O cliente é obrigatório")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false, unique = true)
    private Cliente cliente;
    
    /**
     * MAPA DE RESPOSTAS
     * Armazena as respostas do cliente para cada pergunta
     * Chave: ID da pergunta, Valor: ID da alternativa selecionada
     * Usa ElementCollection para persistir o mapa
     */
    @ElementCollection
    @CollectionTable(
        name = "respostas_quiz_detalhes",
        joinColumns = @JoinColumn(name = "resposta_quiz_id")
    )
    @MapKeyColumn(name = "pergunta")
    @Column(name = "resposta", length = 200)
    private Map<String, String> respostas = new HashMap<>();
    
    /**
     * CRITÉRIO GERADO
     * Critério de personalização gerado automaticamente
     * baseado nas respostas do cliente
     */
    @Column(name = "criterio_gerado", length = 100)
    private String criterioGerado;
    
    /**
     * DATA DE CRIAÇÃO DA RESPOSTA
     * Timestamp de quando a resposta foi criada
     * Não pode ser atualizado após a criação
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    /**
     * DATA DE ATUALIZAÇÃO DA RESPOSTA
     * Timestamp da última atualização da resposta
     */
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    /**
     * CONSTRUTOR PADRÃO
     */
    public RespostaQuiz() {}
    
    /**
     * CONSTRUTOR COMPLETO
     * 
     * @param id ID da resposta
     * @param cliente Cliente que respondeu
     * @param respostas Mapa de respostas
     * @param criterioGerado Critério gerado
     * @param createdAt Data de criação
     * @param updatedAt Data de atualização
     */
    public RespostaQuiz(Long id, Cliente cliente, Map<String, String> respostas, String criterioGerado, Date createdAt, Date updatedAt) {
        this.id = id;
        this.cliente = cliente;
        this.respostas = respostas;
        this.criterioGerado = criterioGerado;
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