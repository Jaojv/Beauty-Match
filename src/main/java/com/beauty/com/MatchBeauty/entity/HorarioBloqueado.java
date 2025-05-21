package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "horario_bloqueado")
public class HorarioBloqueado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario_bloqueado")
    private Long idHorarioBloqueado;

    @ManyToOne
    @JoinColumn(name = "profissional_id", referencedColumnName = "id_usuario", nullable = false)
    private Profissional profissional;

    @ManyToOne
    @JoinColumn(name = "salao_id", referencedColumnName = "id_salao", nullable = false)
    private Salao salao;

    // Getters e setters
    // ...
} 