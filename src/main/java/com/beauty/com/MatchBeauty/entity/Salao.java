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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getHorarioFuncionamento() {
        return horarioFuncionamento;
    }

    public void setHorarioFuncionamento(String horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }

    public Usuario getProprietario() {
        return proprietario;
    }

    public void setProprietario(Usuario proprietario) {
        this.proprietario = proprietario;
    }

    public List<Servico> getServicos() {
        return servicos;
    }

    public void setServicos(List<Servico> servicos) {
        this.servicos = servicos;
    }

    public List<Agendamento> getAgendamentos() {
        return agendamentos;
    }

    public void setAgendamentos(List<Agendamento> agendamentos) {
        this.agendamentos = agendamentos;
    }
}