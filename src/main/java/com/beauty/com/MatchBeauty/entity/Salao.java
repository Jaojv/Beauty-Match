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

    public Salao(Long idSalao, String nome, String endereco, String telefone, Proprietario proprietario) {
        this.idSalao = idSalao;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.proprietario = proprietario;
    }

    public Long getIdSalao() {
        return idSalao;
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

    public Proprietario getProprietario() {
        return proprietario;
    }

    public void setProprietario(Proprietario proprietario) {
        this.proprietario = proprietario;
    }
}