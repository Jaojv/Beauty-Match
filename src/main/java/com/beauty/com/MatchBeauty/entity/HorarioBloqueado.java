package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * ENTIDADE HORÁRIO BLOQUEADO - REPRESENTA BLOQUEIOS TEMPORÁRIOS
 * 
 * Esta entidade representa horários bloqueados para profissionais ou salões.
 * Usado para marcar períodos específicos onde não é possível fazer agendamentos,
 * como férias, feriados, ou ausências temporárias.
 * 
 * RELACIONAMENTOS:
 * - Um bloqueio pertence a um profissional (ManyToOne com Profissional)
 * - Um bloqueio pertence a um salão (ManyToOne com Salao)
 * 
 * CARACTERÍSTICAS:
 * - Data e hora de início do bloqueio
 * - Data e hora de fim do bloqueio
 * - Pode ser aplicado a profissionais específicos ou salões
 * - Usado para validação de agendamentos
 *
 */
@Entity
@Table(name = "horario_bloqueado")
public class HorarioBloqueado {
    
    /**
     * ID ÚNICO DO HORÁRIO BLOQUEADO
     * Chave primária auto-incrementada
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario_bloqueado")
    private Long id;

    /**
     * PROFISSIONAL DO BLOQUEIO
     * Relacionamento ManyToOne com a entidade Profissional
     * Cada bloqueio pode estar associado a um profissional específico
     */
    @ManyToOne
    @JoinColumn(name = "profissional_id", referencedColumnName = "id_usuario", nullable = false)
    private Profissional profissional;

    /**
     * SALÃO DO BLOQUEIO
     * Relacionamento ManyToOne com a entidade Salao
     * Cada bloqueio pode estar associado a um salão específico
     */
    @ManyToOne
    @JoinColumn(name = "salao_id", referencedColumnName = "id_salao", nullable = false)
    private Salao salao;

    /**
     * DATA E HORA DE INÍCIO DO BLOQUEIO
     * Momento exato em que o bloqueio começa
     */
    @Column(name = "data_hora_inicio", nullable = false)
    private LocalDateTime dataHoraInicio;

    /**
     * DATA E HORA DE FIM DO BLOQUEIO
     * Momento exato em que o bloqueio termina
     */
    @Column(name = "data_hora_fim", nullable = false)
    private LocalDateTime dataHoraFim;

    // ========== GETTERS E SETTERS ==========
    // Métodos para acessar e modificar os atributos da classe

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }

    public Salao getSalao() {
        return salao;
    }

    public void setSalao(Salao salao) {
        this.salao = salao;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }
} 