package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "horario_funcionamento_salao")
@Data
public class HorarioFuncionamentoSalao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario_funcionamento")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "salao_id", nullable = false)
    private Salao salao;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false)
    private DayOfWeek diaSemana;
    
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;
    
    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;
    
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
    
    public HorarioFuncionamentoSalao() {}
    
    public HorarioFuncionamentoSalao(Salao salao, DayOfWeek diaSemana, LocalTime horaInicio, LocalTime horaFim) {
        this.salao = salao;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.ativo = true;
    }
    
    // Getters e Setters
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