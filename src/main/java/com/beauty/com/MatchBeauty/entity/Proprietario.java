package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("PROPRIETARIO")
public class Proprietario extends Usuario {
    public Proprietario(Long idUsuario, String username, String password, String email, String telefone, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        super(idUsuario, username, password, email, telefone, criadoEm, atualizadoEm);
    }
    // Pode adicionar métodos específicos de Proprietario se necessário
} 