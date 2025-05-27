package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "agendamento")
@Data
@NoArgsConstructor
public class Agendamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDateTime dataHora;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgendamento status;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;
    
    @ManyToOne
    @JoinColumn(name = "profissional_id", nullable = false)
    private Usuario profissional;
    
    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;
    
    @ManyToOne
    @JoinColumn(name = "salao_id", nullable = false)
    private Salao salao;
    
    @Column
    private String observacoes;
    
    public enum StatusAgendamento {
        AGENDADO,
        CONFIRMADO,
        CANCELADO,
        CONCLUIDO,
        FALTANTE
    }

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
}
