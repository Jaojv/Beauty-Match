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
@Table(name = "salao")
public class Salao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_salao")
    private Long idSalao;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private String telefone;

    @ManyToOne
    @JoinColumn(name = "proprietario_id", referencedColumnName = "id_usuario", nullable = false)
    private Proprietario proprietario;

    // Getters e setters
    // ...
} 