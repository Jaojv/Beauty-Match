package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ENTIDADE AGENDAMENTO - REPRESENTA UM AGENDAMENTO DE SERVIÇO
 * 
 * Esta entidade representa um agendamento de serviço no sistema Match Beauty.
 * Cada agendamento conecta um cliente, um profissional, um serviço e um salão
 * em uma data e hora específica.
 * 
 * RELACIONAMENTOS:
 * - Um agendamento pertence a um cliente (ManyToOne)
 * - Um agendamento pertence a um profissional (ManyToOne)
 * - Um agendamento pertence a um serviço (ManyToOne)
 * - Um agendamento pertence a um salão (ManyToOne)
 * 
 * CARACTERÍSTICAS:
 * - Data e hora específicas para o agendamento
 * - Status para controle do ciclo de vida
 * - Observações opcionais para detalhes adicionais
 * - Validações de disponibilidade e conflitos
 *
 */
@Entity
@Table(name = "agendamento")
public class Agendamento {
    
    /**
     * ID ÚNICO DO AGENDAMENTO
     * Chave primária auto-incrementada
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * DATA E HORA DO AGENDAMENTO
     * Momento específico em que o serviço será realizado
     */
    @Column(nullable = false)
    private LocalDateTime dataHora;
    
    /**
     * CLIENTE DO AGENDAMENTO
     * Usuário que solicitou o serviço
     */
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    /**
     * PROFISSIONAL DO AGENDAMENTO
     * Usuário que realizará o serviço
     */
    @ManyToOne
    @JoinColumn(name = "profissional_id", nullable = false)
    private Usuario profissional;

    /**
     * SERVIÇO DO AGENDAMENTO
     * Serviço específico que será realizado
     */
    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    /**
     * SALÃO DO AGENDAMENTO
     * Estabelecimento onde o serviço será realizado
     */
    @ManyToOne
    @JoinColumn(name = "salao_id", nullable = false)
    private Salao salao;

    /**
     * OBSERVAÇÕES DO AGENDAMENTO
     * Campo opcional para informações adicionais
     */
    @Column
    private String observacoes;

    /**
     * STATUS DO AGENDAMENTO
     * Controla o estado atual do agendamento
     * Valor padrão: AGENDADO
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgendamento status = StatusAgendamento.AGENDADO;

    /**
     * ENUMERATION DOS STATUS DE AGENDAMENTO
     * Define os possíveis estados de um agendamento
     */
    public enum StatusAgendamento {
        AGENDADO,    // Agendamento confirmado
        CONCLUIDO,   // Serviço realizado com sucesso
        CANCELADO,   // Agendamento cancelado
        FALTANTE     // Cliente não compareceu
    }

    /**
     * CONSTRUTOR PADRÃO
     */
    public Agendamento() {}

    /**
     * CONSTRUTOR COMPLETO
     * 
     * @param id ID do agendamento
     * @param dataHora Data e hora do agendamento
     * @param status Status atual do agendamento
     * @param cliente Cliente do agendamento
     * @param profissional Profissional do agendamento
     * @param servico Serviço do agendamento
     * @param salao Salão do agendamento
     * @param observacoes Observações do agendamento
     */
    public Agendamento(Long id, LocalDateTime dataHora, StatusAgendamento status, Usuario cliente, Usuario profissional, Servico servico, Salao salao, String observacoes) {
        this.id = id;
        this.dataHora = dataHora;
        this.status = status;
        this.cliente = cliente;
        this.profissional = profissional;
        this.servico = servico;
        this.salao = salao;
        this.observacoes = observacoes;
    }

    // ========== GETTERS E SETTERS ==========
    // Métodos para acessar e modificar os atributos da classe

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public Usuario getProfissional() {
        return profissional;
    }

    public void setProfissional(Usuario profissional) {
        this.profissional = profissional;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Salao getSalao() {
        return salao;
    }

    public void setSalao(Salao salao) {
        this.salao = salao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
