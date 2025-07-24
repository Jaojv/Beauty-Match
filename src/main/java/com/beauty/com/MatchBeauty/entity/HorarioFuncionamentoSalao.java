package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * ENTIDADE HORÁRIO DE FUNCIONAMENTO DO SALÃO
 * 
 * Esta entidade representa os horários de funcionamento de um salão de beleza.
 * Define em quais dias da semana e em quais horários o salão está aberto
 * para atendimento ao público.
 * 
 * RELACIONAMENTOS:
 * - Um horário pertence a um salão (ManyToOne com Salao)
 * - Um salão pode ter vários horários (OneToMany via Salao)
 * 
 * CARACTERÍSTICAS:
 * - Dia da semana específico (MONDAY, TUESDAY, etc.)
 * - Horário de início e fim do funcionamento
 * - Status ativo/inativo para controle
 * - Usado para validação de agendamentos
 *
 */
@Entity
@Table(name = "horario_funcionamento_salao")
@Data
public class HorarioFuncionamentoSalao {
    
    /**
     * ID ÚNICO DO HORÁRIO DE FUNCIONAMENTO
     * Chave primária auto-incrementada
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario_funcionamento")
    private Long id;
    
    /**
     * SALÃO DO HORÁRIO DE FUNCIONAMENTO
     * Relacionamento ManyToOne com a entidade Salao
     * Cada horário pertence a um salão específico
     */
    @ManyToOne
    @JoinColumn(name = "salao_id", nullable = false)
    private Salao salao;
    
    /**
     * DIA DA SEMANA
     * Dia específico da semana para este horário
     * Ex: MONDAY, TUESDAY, WEDNESDAY, etc.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false)
    private DayOfWeek diaSemana;
    
    /**
     * HORA DE INÍCIO DO FUNCIONAMENTO
     * Horário em que o salão abre neste dia
     */
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;
    
    /**
     * HORA DE FIM DO FUNCIONAMENTO
     * Horário em que o salão fecha neste dia
     */
    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;
    
    /**
     * STATUS ATIVO DO HORÁRIO
     * Controla se este horário está ativo para agendamentos
     * Valor padrão: true
     */
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
    
    /**
     * CONSTRUTOR PADRÃO
     */
    public HorarioFuncionamentoSalao() {}
    
    /**
     * CONSTRUTOR COM PARÂMETROS
     * 
     * @param salao Salão do horário
     * @param diaSemana Dia da semana
     * @param horaInicio Hora de início
     * @param horaFim Hora de fim
     */
    public HorarioFuncionamentoSalao(Salao salao, DayOfWeek diaSemana, LocalTime horaInicio, LocalTime horaFim) {
        this.salao = salao;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
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
    
    public Salao getSalao() {
        return salao;
    }
    
    public void setSalao(Salao salao) {
        this.salao = salao;
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
    
    public Boolean getAtivo() {
        return ativo;
    }
    
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
} 