package com.beauty.com.MatchBeauty.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "servico")
public class Servico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servico")
    private Long idServico;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column(name = "duracao_em_minutos", nullable = false)
    private Integer duracaoEmMinutos;

    @ManyToOne
    @JoinColumn(name = "salao_id", referencedColumnName = "id_salao", nullable = false)
    private Salao salao;

    // Getters e setters
    // ...
} 