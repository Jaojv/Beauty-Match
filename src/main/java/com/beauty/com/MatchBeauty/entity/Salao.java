package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.ArrayList;

/**
 * ENTIDADE SALÃO - REPRESENTA UM SALÃO DE BELEZA NO SISTEMA
 * 
 * Esta entidade representa um salão de beleza no sistema Match Beauty.
 * Cada salão possui informações básicas como nome, endereço, telefone,
 * e está associado a um proprietário, profissionais, serviços e agendamentos.
 * 
 * RELACIONAMENTOS:
 * - Um salão pertence a um proprietário (ManyToOne)
 * - Um salão pode ter vários horários de funcionamento (OneToMany)
 * - Um salão pode oferecer vários serviços (OneToMany)
 * - Um salão pode ter vários agendamentos (OneToMany)
 * - Um salão pode ter vários profissionais (OneToMany)
 *
 */
@Entity
@Table(name = "salao")
@Data
public class Salao {
    
    /**
     * ID ÚNICO DO SALÃO
     * Chave primária auto-incrementada
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_salao")
    private Long id;
    
    /**
     * NOME DO SALÃO
     * Nome comercial do estabelecimento
     */
    @Column(nullable = false)
    private String nome;
    
    /**
     * ENDEREÇO DO SALÃO
     * Endereço completo do estabelecimento
     */
    @Column(nullable = false)
    private String endereco;
    
    /**
     * TELEFONE DO SALÃO
     * Telefone para contato
     */
    @Column(nullable = false)
    private String telefone;
    
    /**
     * EMAIL DO SALÃO
     * Email para contato comercial
     */
    @Column(nullable = false)
    private String email;
    
    /**
     * DESCRIÇÃO DO SALÃO
     * Informações sobre o estabelecimento, serviços oferecidos, etc.
     */
    @Column(nullable = false)
    private String descricao;
    
    /**
     * URL DA IMAGEM DO SALÃO
     * Caminho para a imagem/foto do estabelecimento
     */
    @Column
    private String imagemUrl;
    
    /**
     * PROPRIETÁRIO DO SALÃO
     * Relacionamento ManyToOne com a entidade Usuario
     * Cada salão pertence a um proprietário
     */
    @ManyToOne
    @JoinColumn(name = "proprietario_id", nullable = false)
    private Usuario proprietario;
    
    /**
     * HORÁRIOS DE FUNCIONAMENTO DO SALÃO
     * Lista de horários de funcionamento para cada dia da semana
     * Relacionamento OneToMany com HorarioFuncionamentoSalao
     */
    @OneToMany(mappedBy = "salao", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<HorarioFuncionamentoSalao> horariosFuncionamento = new ArrayList<>();
    
    /**
     * SERVIÇOS OFERECIDOS PELO SALÃO
     * Lista de serviços disponíveis no estabelecimento
     * Relacionamento OneToMany com Servico
     */
    @OneToMany(mappedBy = "salao")
    private List<Servico> servicos = new ArrayList<>();
    
    /**
     * AGENDAMENTOS DO SALÃO
     * Lista de todos os agendamentos realizados no estabelecimento
     * Relacionamento OneToMany com Agendamento
     */
    @OneToMany(mappedBy = "salao")
    private List<Agendamento> agendamentos = new ArrayList<>();

    /**
     * PROFISSIONAIS DO SALÃO
     * Lista de profissionais que trabalham no estabelecimento
     * Relacionamento OneToMany com Profissional
     */
    @OneToMany(mappedBy = "salao")
    private List<Profissional> profissionais;

    /**
     * CONSTRUTOR PADRÃO
     * Inicializa as listas vazias para evitar NullPointerException
     */
    public Salao() {
        this.servicos = new ArrayList<>();
        this.agendamentos = new ArrayList<>();
        this.horariosFuncionamento = new ArrayList<>();
    }

    // ========== GETTERS E SETTERS ==========
    // Métodos para acessar e modificar os atributos da classe

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public Usuario getProprietario() {
        return proprietario;
    }

    public void setProprietario(Usuario proprietario) {
        this.proprietario = proprietario;
    }

    public List<HorarioFuncionamentoSalao> getHorariosFuncionamento() {
        return horariosFuncionamento;
    }

    public void setHorariosFuncionamento(List<HorarioFuncionamentoSalao> horariosFuncionamento) {
        this.horariosFuncionamento = horariosFuncionamento;
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

    public List<Profissional> getProfissionais() {
        return profissionais;
    }

    public void setProfissionais(List<Profissional> profissionais) {
        this.profissionais = profissionais;
    }
}