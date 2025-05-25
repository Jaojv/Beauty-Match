package com.beauty.com.MatchBeauty.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "proprietario")
public class Proprietario extends Usuario {
    
    @Column(name = "cnpj")
    private String cnpj;
    
    @Column(name = "razao_social")
    private String razaoSocial;
    
    @Column(name = "endereco")
    private String endereco;
    
    @Column(name = "horario_funcionamento")
    private String horarioFuncionamento;
    
    public Proprietario() {
        super();
    }

    public Proprietario(Long idUsuario, String username, String password, String email, String telefone, 
                       LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        super(idUsuario, username, password, email, telefone, criadoEm, atualizadoEm);
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getHorarioFuncionamento() {
        return horarioFuncionamento;
    }

    public void setHorarioFuncionamento(String horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }

    // Métodos específicos do Proprietario
    public void atualizarDados(String nome, String email, String telefone) {
        this.setNome(nome);
        this.setEmail(email);
        this.setTelefone(telefone);
        this.setAtualizadoEm(LocalDateTime.now());
    }
} 