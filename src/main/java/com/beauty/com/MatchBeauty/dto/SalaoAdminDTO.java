package com.beauty.com.MatchBeauty.dto;

public class SalaoAdminDTO {
    private Long id;
    private String nome;
    private String descricao;
    private String email;
    private String telefone;
    private String endereco;
    private String status;
    private String proprietarioNome;
    private String proprietarioEmail;
    private String proprietarioTelefone;
    private Long proprietarioId;

    // Construtor padrão
    public SalaoAdminDTO() {}

    // Construtor completo
    public SalaoAdminDTO(Long id, String nome, String descricao, String email, 
                        String telefone, String endereco, String status, 
                        String proprietarioNome, String proprietarioEmail, 
                        String proprietarioTelefone, Long proprietarioId) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.status = status;
        this.proprietarioNome = proprietarioNome;
        this.proprietarioEmail = proprietarioEmail;
        this.proprietarioTelefone = proprietarioTelefone;
        this.proprietarioId = proprietarioId;
    }

    // Getters e Setters
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProprietarioNome() {
        return proprietarioNome;
    }

    public void setProprietarioNome(String proprietarioNome) {
        this.proprietarioNome = proprietarioNome;
    }

    public String getProprietarioEmail() {
        return proprietarioEmail;
    }

    public void setProprietarioEmail(String proprietarioEmail) {
        this.proprietarioEmail = proprietarioEmail;
    }

    public String getProprietarioTelefone() {
        return proprietarioTelefone;
    }

    public void setProprietarioTelefone(String proprietarioTelefone) {
        this.proprietarioTelefone = proprietarioTelefone;
    }

    public Long getProprietarioId() {
        return proprietarioId;
    }

    public void setProprietarioId(Long proprietarioId) {
        this.proprietarioId = proprietarioId;
    }
} 