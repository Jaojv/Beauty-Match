package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * ENTIDADE HORÁRIO DE TRABALHO DO PROFISSIONAL
 * 
 * Esta entidade representa os horários de trabalho de um profissional.
 * Define em quais dias da semana e em quais horários o profissional
 * está disponível para atendimento.
 * 
 * RELACIONAMENTOS:
 * - Um horário pertence a um profissional (ManyToOne com Usuario)
 * - Um profissional pode ter vários horários (OneToMany via Profissional)
 * 
 * CARACTERÍSTICAS:
 * - Dia da semana específico (MONDAY, TUESDAY, etc.)
 * - Horário de início e fim do trabalho
 * - Status ativo/inativo para controle
 * - Status bloqueado para bloqueios temporários
 * - Observações para detalhes específicos
 *
 */
@Entity
@Table(name = "horario_trabalho")
public class HorarioTrabalho {
    
    /**
     * ID ÚNICO DO HORÁRIO DE TRABALHO
     * Chave primária auto-incrementada
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * PROFISSIONAL DO HORÁRIO DE TRABALHO
     * Relacionamento ManyToOne com a entidade Usuario
     * Cada horário pertence a um profissional específico
     */
    @ManyToOne
    @JoinColumn(name = "profissional_id", nullable = false)
    @JsonIgnore
    private Usuario profissional;
    
    /**
     * DIA DA SEMANA
     * Dia específico da semana para este horário de trabalho
     * Ex: MONDAY, TUESDAY, WEDNESDAY, etc.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek diaSemana;
    
    /**
     * HORA DE INÍCIO DO TRABALHO
     * Horário em que o profissional inicia o trabalho neste dia
     */
    @Column(nullable = false)
    private LocalTime horaInicio;
    
    /**
     * HORA DE FIM DO TRABALHO
     * Horário em que o profissional finaliza o trabalho neste dia
     */
    @Column(nullable = false)
    private LocalTime horaFim;
    
    /**
     * STATUS ATIVO DO HORÁRIO
     * Controla se este horário está ativo para agendamentos
     * Valor padrão: true
     */
    @Column(nullable = false)
    private Boolean ativo = true;
    
    /**
     * STATUS BLOQUEADO DO HORÁRIO
     * Controla se este horário está temporariamente bloqueado
     * Valor padrão: false
     */
    @Column(nullable = false)
    private Boolean bloqueado = false;
    
    /**
     * OBSERVAÇÕES DO HORÁRIO
     * Campo opcional para informações adicionais sobre o horário
     */
    @Column
    private String observacoes;
    
    /**
     * CONSTRUTOR PADRÃO
     */
    public HorarioTrabalho() {
    }

    /**
     * CONSTRUTOR COM PARÂMETROS
     * 
     * @param profissional Profissional do horário
     * @param diaSemana Dia da semana
     * @param horaInicio Hora de início
     * @param horaFim Hora de fim
     */
    public HorarioTrabalho(Usuario profissional, DayOfWeek diaSemana, LocalTime horaInicio, LocalTime horaFim) {
        this.profissional = profissional;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.bloqueado = false;
        this.ativo = true;
    }

    // ========== GETTERS E SETTERS ==========
    // Métodos para acessar e modificar os atributos da classe

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getProfissional() {
        return profissional;
    }

    public void setProfissional(Usuario profissional) {
        this.profissional = profissional;
    }

    public DayOfWeek getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DayOfWeek diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
} 