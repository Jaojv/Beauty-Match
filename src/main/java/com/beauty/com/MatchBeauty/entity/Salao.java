package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "salao")
@Data
@NoArgsConstructor
public class Salao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_salao")
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

    public Salao(Long id, String nome, String endereco, String telefone, String descricao, String horarioFuncionamento, Usuario proprietario, List<Servico> servicos, List<Agendamento> agendamentos) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.descricao = descricao;
        this.horarioFuncionamento = horarioFuncionamento;
        this.proprietario = proprietario;
        this.servicos = servicos;
        this.agendamentos = agendamentos;
    }
}