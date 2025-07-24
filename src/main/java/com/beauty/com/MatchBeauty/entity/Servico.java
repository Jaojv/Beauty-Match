package com.beauty.com.MatchBeauty.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ENTIDADE SERVIÇO - REPRESENTA UM SERVIÇO OFERECIDO PELO SALÃO
 * 
 * Esta entidade representa um serviço oferecido por um salão de beleza.
 * Cada serviço possui informações como nome, descrição, preço, duração
 * e está associado a um salão específico.
 * 
 * RELACIONAMENTOS:
 * - Um serviço pertence a um salão (ManyToOne)
 * - Um serviço pode ter vários agendamentos (OneToMany)
 * 
 * CARACTERÍSTICAS:
 * - Preço em BigDecimal para precisão monetária
 * - Duração em minutos para cálculo de horários
 * - Status ativo/inativo para controle de disponibilidade
 * - Imagem opcional para exibição visual
 *
 */
@Entity
@Table(name = "servico")
public class Servico {
    
    /**
     * ID ÚNICO DO SERVIÇO
     * Chave primária auto-incrementada
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * NOME DO SERVIÇO
     * Nome comercial do serviço oferecido
     */
    @Column(nullable = false)
    private String nome;

    /**
     * DESCRIÇÃO DO SERVIÇO
     * Detalhes sobre o que o serviço inclui
     */
    @Column(nullable = false)
    private String descricao;

    /**
     * PREÇO DO SERVIÇO
     * Valor cobrado pelo serviço (usando BigDecimal para precisão)
     */
    @Column(nullable = false)
    private BigDecimal preco;

    /**
     * DURAÇÃO DO SERVIÇO EM MINUTOS
     * Tempo necessário para realizar o serviço
     */
    @Column(nullable = false)
    private Integer duracaoMinutos;

    /**
     * SALÃO QUE OFERECE O SERVIÇO
     * Relacionamento ManyToOne com a entidade Salao
     */
    @ManyToOne
    @JoinColumn(name = "salao_id", nullable = false)
    private Salao salao;

    /**
     * IMAGEM DO SERVIÇO
     * URL da imagem representativa do serviço
     */
    @Column
    private String imagem;

    /**
     * STATUS ATIVO DO SERVIÇO
     * Controla se o serviço está disponível para agendamento
     * Valor padrão: true
     */
    @Column(nullable = false)
    private Boolean ativo = true;

    /**
     * AGENDAMENTOS DO SERVIÇO
     * Lista de agendamentos que utilizam este serviço
     * Relacionamento OneToMany com Agendamento
     */
    @OneToMany(mappedBy = "servico")
    private List<Agendamento> agendamentos = new ArrayList<>();

    /**
     * CONSTRUTOR PADRÃO
     * Inicializa a lista de agendamentos vazia
     */
    public Servico() {
        this.agendamentos = new ArrayList<>();
    }

    /**
     * CONSTRUTOR COMPLETO
     * 
     * @param id ID do serviço
     * @param nome Nome do serviço
     * @param descricao Descrição do serviço
     * @param preco Preço do serviço
     * @param duracaoMinutos Duração em minutos
     * @param salao Salão que oferece o serviço
     * @param agendamentos Lista de agendamentos
     */
    public Servico(Long id, String nome, String descricao, BigDecimal preco, Integer duracaoMinutos, Salao salao, List<Agendamento> agendamentos) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.duracaoMinutos = duracaoMinutos;
        this.salao = salao;
        this.agendamentos = agendamentos;
    }

    // ========== GETTERS E SETTERS ==========
    // Métodos para acessar e modificar os atributos da classe

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Salao getSalao() {
        return salao;
    }

    public void setSalao(Salao salao) {
        this.salao = salao;
    }

    public List<Agendamento> getAgendamentos() {
        return agendamentos;
    }

    public void setAgendamentos(List<Agendamento> agendamentos) {
        this.agendamentos = agendamentos;
    }
}