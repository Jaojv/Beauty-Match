package com.beauty.com.MatchBeauty.entity;

import java.time.LocalDateTime;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PROPRIETARIO")
public class Proprietario extends Usuario {
    
    public Proprietario() {
        super();
    }

    public Proprietario(Long idUsuario, String username, String password, String email, String nome, String telefone, 
                       LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        super(idUsuario, username, password, email, nome, telefone, criadoEm, atualizadoEm);
    }

    // Métodos específicos do Proprietario
    public void atualizarDados(String nome, String email, String telefone) {
        this.setNome(nome);
        this.setEmail(email);
        this.setTelefone(telefone);
        this.setAtualizadoEm(LocalDateTime.now());
    }
} 