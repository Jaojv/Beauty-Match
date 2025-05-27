package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "salao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false)
    private String endereco;
    
    @Column(nullable = false)
    private String telefone;
    
    @Column
    private String descricao;
    
    @Column
    private String horarioFuncionamento;
    
    @ManyToOne
    @JoinColumn(name = "proprietario_id", nullable = false)
    private Usuario proprietario;
    
    @OneToMany(mappedBy = "salao", cascade = CascadeType.ALL)
    private List<Servico> servicos;
    
    @OneToMany(mappedBy = "salao", cascade = CascadeType.ALL)
    private List<Agendamento> agendamentos;
}