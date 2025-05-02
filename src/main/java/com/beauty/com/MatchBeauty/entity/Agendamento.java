package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "agendamento")
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agendamento")
    private Long idAgendamento;

    @ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "id_usuario", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "profissional_id", referencedColumnName = "id_usuario", nullable = false)
    private Profissional profissional;

    @ManyToOne
    @JoinColumn(name = "servico_id", referencedColumnName = "id_servico", nullable = false)
    private Servico servico;

    @ManyToOne
    @JoinColumn(name = "salao_id", referencedColumnName = "id_salao", nullable = false)
    private Salao salao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgendamento status;

    // Getters e setters
    // ...
} 