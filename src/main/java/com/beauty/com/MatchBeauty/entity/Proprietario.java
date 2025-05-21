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

    public Proprietario(Long idUsuario, String username, String password, String email, String telefone, 
                       LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        super(idUsuario, username, password, email, telefone, criadoEm, atualizadoEm);
    }
    // Pode adicionar métodos específicos de Proprietario se necessário
} 